package com.example.clinic.ui.doctor.bookings

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.clinic.R
import com.example.clinic.data.DoctorBooking
import com.example.clinic.repos.PatientRepo
import com.example.clinic.utils.ConstData
import com.google.firebase.auth.FirebaseAuth
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class BookingWorker(private val context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    private val repo = PatientRepo()

    override fun doWork(): Result {
        try {
            // Check if user is authenticated (required by Firebase rules)
            if (FirebaseAuth.getInstance().currentUser == null) {
                Log.d("BookingWorker", "No authenticated user")
                return Result.success()
            }

            val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            val role = sharedPreferences.getString("role", null)

            if (role != ConstData.DOCTOR_TYPE) {
                Log.d("BookingWorker", "Not a doctor, skipping work")
                return Result.success()
            }

            // Use CountDownLatch to wait for Firebase callback
            val latch = CountDownLatch(1)
            var result: Result = Result.success()

            repo.getAllBookings(
                onResult = { bookings ->
                    try {
                        if (bookings.isNotEmpty()) {
                            val latest = bookings.last()
                            val lastNotifiedId = sharedPreferences.getString("last_booking_id", null)

                            if (latest.bookingId != lastNotifiedId) {
                                showNotification(context, latest)
                                sharedPreferences.edit().putString("last_booking_id", latest.bookingId).apply()
                                Log.d("BookingWorker", "Notification sent for booking ID: ${latest.bookingId}")
                            } else {
                                Log.d("BookingWorker", "No new bookings")
                            }
                        } else {
                            Log.d("BookingWorker", "No bookings found")
                        }
                    } finally {
                        latch.countDown() // Release latch after processing
                    }
                },
                onFailure = { error ->
                    Log.e("BookingWorker", "Error fetching bookings: $error")
                    result = Result.retry() // Retry on failure
                    latch.countDown() // Release latch on error
                }
            )

            // Wait for callback to complete (up to 10 seconds)
            latch.await(10, TimeUnit.SECONDS)
            return result
        } catch (e: Exception) {
            Log.e("BookingWorker", "Worker failed: ${e.message}")
            return Result.retry()
        }
    }

    private fun showNotification(context: Context, booking: DoctorBooking) {
        val channelId = "booking_channel_id"
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Bookings",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for new doctor bookings"
            }
            manager.createNotificationChannel(channel)
        }

        val groupKey = "com.example.clinic.BOOKINGS"
        val notificationId = System.currentTimeMillis().toInt()

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.notification)
            .setContentTitle("New Booking")
            .setContentText("${booking.patientName} at ${booking.slot} on ${booking.date}")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setGroup(groupKey)
            .build()

        manager.notify(notificationId, notification)

        val summaryNotification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.notification)
            .setContentTitle("Clinic Bookings")
            .setContentText("You have new bookings")
            .setStyle(NotificationCompat.InboxStyle())
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setGroup(groupKey)
            .setGroupSummary(true)
            .build()

        manager.notify(0, summaryNotification)
    }
}