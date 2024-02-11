/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package image_downloader;

import javax.swing.*;

/**
 *
 * @author asism
 */
public class DownloadTask implements Runnable {

    private String imageUrl;
    private JLabel lblStatus;
    private volatile boolean isPaused;
    private volatile boolean isCancelled;
    private JProgressBar progressBar;

    

    public DownloadTask(String imageUrl, JProgressBar progressBar, JLabel statusLabel) {
        this.imageUrl = imageUrl;
        this.lblStatus = statusLabel;
        this.isPaused = false;
        this.isCancelled = false;
        this.progressBar = progressBar;

    }

    public void setPaused(boolean paused) {
        this.isPaused = paused;
    }

    public void setCancelled(boolean cancelled) {
        this.isCancelled = cancelled;
    }

    @Override
public void run() {
    try {
        // Reset progress bar
        progressBar.setValue(0);

        // Simulate downloading by sleeping for some time
        for (int progress = 0; progress <= 100; progress += 10) {
            progressBar.setValue(progress);
            lblStatus.setText("Downloading... " + progress + "%");

            // Check if download is cancelled
            if (isCancelled) {
                SwingUtilities.invokeLater(() -> lblStatus.setText("Download Cancelled"));
                // Reset progress bar
                progressBar.setValue(0);
                return;
            }

            // Sleep for a short duration to simulate download progress
            for (int i = 0; i < 10 && !isPaused; i++) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // Restore interrupted status
                    Thread.currentThread().interrupt();
                }
            }

            // Check if download is paused
            while (isPaused) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // Restore interrupted status
                    Thread.currentThread().interrupt();
                }
            }
        }

        // After download is complete, update the status label
        SwingUtilities.invokeLater(() -> lblStatus.setText("Download Complete"));
    } catch (Exception e) {
        // Update status label to indicate download error
        SwingUtilities.invokeLater(() -> lblStatus.setText("Download Error"));
    }
}


    boolean isPaused() {

        return isPaused;
    }

}
