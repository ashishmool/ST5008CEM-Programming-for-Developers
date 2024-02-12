package assignment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Q6 extends JFrame {

    private ExecutorService executorService;
    private JPanel downloadPanel;

    private JButton btnStartDownload;
    private JTextField txtImageURL;
    private JButton btnCancelReset;
    private JLabel lblTitle;
    private JLabel lblStatus;

    public Q6() {
        initComponents();
        executorService = Executors.newCachedThreadPool();
    }

    private void initComponents() {
        btnStartDownload = new JButton("Start Download");
        txtImageURL = new JTextField("https://images.pexels.com/photos/2456439/pexels-photo-2456439.jpeg");
        btnCancelReset = new JButton("Cancel All");
        lblTitle = new JLabel("Multithreaded Asynchronous Image Downloader", JLabel.CENTER);
        lblStatus = new JLabel("Downloads:", JLabel.CENTER);

        btnStartDownload.addActionListener(this::btnStartDownloadActionPerformed);
        btnCancelReset.addActionListener(this::btnCancelResetActionPerformed);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(lblTitle, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel controlPanel = new JPanel(new FlowLayout());

        controlPanel.add(btnStartDownload);
        controlPanel.add(btnCancelReset);
        mainPanel.add(controlPanel, BorderLayout.NORTH);

        downloadPanel = new JPanel();
        downloadPanel.setLayout(new BoxLayout(downloadPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(downloadPanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        add(lblStatus, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    private void btnStartDownloadActionPerformed(ActionEvent evt) {
        String imageUrl = txtImageURL.getText();
        DownloadEntry downloadEntry = new DownloadEntry(imageUrl);
        downloadPanel.add(downloadEntry);
        downloadPanel.revalidate();
        downloadPanel.repaint();

        DownloadTask downloadTask = new DownloadTask(imageUrl, downloadEntry);
        executorService.submit(downloadTask);
    }

    private void btnCancelResetActionPerformed(ActionEvent evt) {
        // Cancel all downloads
        Component[] components = downloadPanel.getComponents();
        for (Component component : components) {
            if (component instanceof DownloadEntry) {
                DownloadEntry downloadEntry = (DownloadEntry) component;
                downloadEntry.cancelDownload();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Q6 form = new Q6();
            form.setVisible(true);
        });
    }

    @Override
    public void dispose() {
        super.dispose();
        executorService.shutdown();
    }
}

class DownloadEntry extends JPanel {

    private String imageUrl;
    private JLabel lblUrl;
    private JProgressBar progressBar;
    private JButton btnPause;
    private JButton btnCancel;
    private volatile boolean isPaused;
    private volatile boolean isCancelled;

    public DownloadEntry(String imageUrl) {
        this.imageUrl = imageUrl;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        lblUrl = new JLabel(imageUrl);
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        btnPause = new JButton("Pause");
        btnCancel = new JButton("Cancel");

        btnPause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                togglePause();
            }
        });

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelDownload();
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(btnPause);
        buttonPanel.add(btnCancel);

        add(lblUrl, BorderLayout.NORTH);
        add(progressBar, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void setProgress(int progress) {
        progressBar.setValue(progress);
        if (progress == 100) {
            removeEntry();
        }
    }

    public void togglePause() {
        isPaused = !isPaused;
    }

    public void cancelDownload() {
        isCancelled = true;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    private void removeEntry() {
        SwingUtilities.invokeLater(() -> {
            Container parent = getParent();
            parent.remove(this);
            parent.revalidate();
            parent.repaint();
        });
    }
}

class DownloadTask implements Runnable {

    private static final int TOTAL_BYTES = 1000; // Dummy total bytes for download
    private static final int DOWNLOAD_INCREMENT = 10; // Increment in downloaded bytes

    private String imageUrl;
    private DownloadEntry downloadEntry;

    public DownloadTask(String imageUrl, DownloadEntry downloadEntry) {
        this.imageUrl = imageUrl;
        this.downloadEntry = downloadEntry;
    }

    @Override
    public void run() {
        int downloadedBytes = 0;
        try {
            while (downloadedBytes < TOTAL_BYTES) {
                if (downloadEntry.isCancelled()) {
                    downloadEntry.setProgress(0);
                    return;
                }
                if (!downloadEntry.isPaused()) {
                    int progress = (int) ((double) downloadedBytes / TOTAL_BYTES * 100);
                    SwingUtilities.invokeLater(() -> downloadEntry.setProgress(progress));
                    Thread.sleep(100); // Simulating download delay
                    downloadedBytes += DOWNLOAD_INCREMENT; // Increment downloaded bytes
                }
            }
            // Set progress to 100% when download is complete
            SwingUtilities.invokeLater(() -> downloadEntry.setProgress(100));
        } catch (InterruptedException e) {
            // Handle interruption
        }
    }
}
