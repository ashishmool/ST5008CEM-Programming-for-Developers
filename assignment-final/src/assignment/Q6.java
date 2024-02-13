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
        setSize(800, 600); // Set initial size of the window

    }

    private void initComponents() {
        btnStartDownload = new JButton("Start Download");
        txtImageURL = new JTextField("https://images.pexels.com/photos/2456439/pexels-photo-2456439.jpeg");
        txtImageURL.setVisible(true); // Make the text field visible
        txtImageURL.setEditable(true); // Make the text field editable
        btnCancelReset = new JButton("Cancel All");
        lblTitle = new JLabel("Multithreaded Asynchronous Image Downloader by 220417", JLabel.CENTER);
        lblStatus = new JLabel("Downloads:", JLabel.CENTER);

        btnStartDownload.addActionListener(this::btnStartDownloadActionPerformed);
        btnCancelReset.addActionListener(this::btnCancelResetActionPerformed);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(lblTitle, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel controlPanel = new JPanel(new FlowLayout());

        controlPanel.add(btnStartDownload);
        controlPanel.add(txtImageURL); // Add the text field to the control panel

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

    private boolean isValidImageUrl(String url) {
        // Define a list of valid image extensions
        String[] validExtensions = {".jpg", ".jpeg", ".png", ".tif", ".svg", ".webp", ".tiff", ".bmp"};

        // Check if the URL ends with a valid image extension
        for (String extension : validExtensions) {
            if (url.toLowerCase().endsWith(extension)) {
                return true;
            }
        }
        return false;
    }


    private void btnStartDownloadActionPerformed(ActionEvent evt) {
        String imageUrl = txtImageURL.getText();

        // Check if the URL is valid
        if (!isValidImageUrl(imageUrl)) {
            JOptionPane.showMessageDialog(this, "Enter Valid Image URL", "Invalid URL", JOptionPane.ERROR_MESSAGE);
            return;
        }



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
        if (isPaused) {
            btnPause.setText("Resume");
        } else {
            btnPause.setText("Pause");
        }
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
                    Thread.sleep(1000); // Simulating download delay
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
