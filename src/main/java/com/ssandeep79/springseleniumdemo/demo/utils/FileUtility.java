package com.ssandeep79.springseleniumdemo.demo.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * Utility class for file operations
 * Provides methods for file handling, comparison, and verification
 */
public class FileUtility {
    private static final Logger logger = LoggerFactory.getLogger(FileUtility.class);
    private static final int DEFAULT_WAIT_TIME = 30;
    
    private final WebDriver driver;

    public FileUtility(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Waits for a file to be downloaded to the specified directory
     * @param directory the download directory
     * @param filePattern regex pattern to match the downloaded file
     * @param timeoutInSeconds maximum time to wait in seconds
     * @return the downloaded file if found, null otherwise
     */
    public File waitForFileDownload(String directory, String filePattern, int timeoutInSeconds) {
        File dir = new File(directory);
        long endTime = System.currentTimeMillis() + timeoutInSeconds * 1000L;
        Pattern pattern = Pattern.compile(filePattern);
        
        while (System.currentTimeMillis() < endTime) {
            File[] files = dir.listFiles((d, name) -> pattern.matcher(name).matches());
            
            if (files != null && files.length > 0) {
                return files[0];
            }
            
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.warn("Thread was interrupted while waiting for file download", e);
                return null;
            }
        }
        
        logger.warn("Timeout waiting for file download. No file matching '{}' found in {}", filePattern, directory);
        return null;
    }

    /**
     * Gets MD5 checksum of a file
     * @param file the file to calculate checksum for
     * @return MD5 checksum as hexadecimal string
     * @throws IOException if file cannot be read
     * @throws NoSuchAlgorithmException if MD5 algorithm is not available
     */
    public static String getFileMD5Checksum(File file) throws IOException, NoSuchAlgorithmException {
        try (InputStream is = Files.newInputStream(file.toPath())) {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[8192];
            int read;
            
            while ((read = is.read(buffer)) > 0) {
                md.update(buffer, 0, read);
            }
            
            byte[] digest = md.digest();
            StringBuilder sb = new StringBuilder();
            
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
            
            return sb.toString();
        }
    }

    /**
     * Compares two files for equality
     * @param file1 first file
     * @param file2 second file
     * @return true if files have identical content
     */
    public static boolean compareFiles(File file1, File file2) {
        try {
            return FileUtils.contentEquals(file1, file2);
        } catch (IOException e) {
            logger.error("Error comparing files: {} and {}", file1.getPath(), file2.getPath(), e);
            return false;
        }
    }

    /**
     * Reads a text file into a string
     * @param filePath path to the file
     * @return file content as string
     * @throws IOException if file cannot be read
     */
    public static String readFileToString(String filePath) throws IOException {
        return FileUtils.readFileToString(new File(filePath), StandardCharsets.UTF_8);
    }

    /**
     * Reads a text file into a list of lines
     * @param filePath path to the file
     * @return list of lines from the file
     * @throws IOException if file cannot be read
     */
    public static List<String> readFileToLines(String filePath) throws IOException {
        return FileUtils.readLines(new File(filePath), StandardCharsets.UTF_8);
    }

    /**
     * Creates a unique temporary file
     * @param prefix file name prefix
     * @param suffix file name suffix
     * @return the created temporary file
     * @throws IOException if file cannot be created
     */
    public static File createTempFile(String prefix, String suffix) throws IOException {
        return File.createTempFile(prefix, suffix);
    }

    /**
     * Gets the file extension
     * @param filePath path to the file
     * @return file extension without the dot
     */
    public static String getFileExtension(String filePath) {
        return FilenameUtils.getExtension(filePath);
    }

    /**
     * Writes text content to a file
     * @param filePath path to the file
     * @param content text content to write
     * @throws IOException if file cannot be written
     */
    public static void writeStringToFile(String filePath, String content) throws IOException {
        FileUtils.writeStringToFile(new File(filePath), content, StandardCharsets.UTF_8);
    }

    /**
     * Executes a command to upload a file using AutoIT or similar tool
     * @param autoItScriptPath path to the AutoIT script
     * @param filePath path to the file to upload
     * @return true if command execution was successful
     */
    public boolean executeFileUploadCommand(String autoItScriptPath, String filePath) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(autoItScriptPath, filePath);
            Process process = processBuilder.start();
            boolean completed = process.waitFor(DEFAULT_WAIT_TIME, TimeUnit.SECONDS);
            
            if (!completed) {
                logger.warn("Upload command timed out after {} seconds", DEFAULT_WAIT_TIME);
                process.destroyForcibly();
                return false;
            }
            
            int exitCode = process.exitValue();
            if (exitCode != 0) {
                logger.warn("Upload command failed with exit code: {}", exitCode);
                return false;
            }
            
            return true;
        } catch (IOException | InterruptedException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            logger.error("Error executing upload command", e);
            return false;
        }
    }

    /**
     * Finds all files in a directory matching a pattern
     * @param directory directory to search in
     * @param filePattern regex pattern to match
     * @param recursive whether to search recursively
     * @return list of matching files
     */
    public static List<File> findFiles(String directory, String filePattern, boolean recursive) {
        File dir = new File(directory);
        List<File> result = new ArrayList<>();
        Pattern pattern = Pattern.compile(filePattern);
        
        findFilesRecursive(dir, pattern, recursive, result);
        return result;
    }

    private static void findFilesRecursive(File directory, Pattern pattern, boolean recursive, List<File> result) {
        File[] files = directory.listFiles();
        
        if (files == null) {
            return;
        }
        
        for (File file : files) {
            if (file.isFile() && pattern.matcher(file.getName()).matches()) {
                result.add(file);
            } else if (recursive && file.isDirectory()) {
                findFilesRecursive(file, pattern, true, result);
            }
        }
    }

    /**
     * Gets the size of a file in bytes
     * @param filePath path to the file
     * @return file size in bytes
     */
    public static long getFileSize(String filePath) {
        Path path = Paths.get(filePath);
        try {
            return Files.size(path);
        } catch (IOException e) {
            logger.error("Error getting file size for {}", filePath, e);
            return -1;
        }
    }

    /**
     * Converts file size to human-readable format
     * @param bytes file size in bytes
     * @return human-readable file size (e.g., "1.23 MB")
     */
    public static String getReadableFileSize(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        }
        
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format("%.2f %sB", bytes / Math.pow(1024, exp), pre);
    }

    /**
     * Deletes a file or directory
     * @param path path to file or directory
     * @return true if deletion was successful
     */
    public static boolean deleteFile(String path) {
        File file = new File(path);
        if (file.isDirectory()) {
            try {
                FileUtils.deleteDirectory(file);
                return true;
            } catch (IOException e) {
                logger.error("Error deleting directory: {}", path, e);
                return false;
            }
        } else {
            return file.delete();
        }
    }

    /**
     * Creates directory if it doesn't exist
     * @param directoryPath path to directory
     * @return true if directory exists or was created successfully
     */
    public static boolean createDirectoryIfNotExists(String directoryPath) {
        File directory = new File(directoryPath);
        if (directory.exists()) {
            return directory.isDirectory();
        } else {
            return directory.mkdirs();
        }
    }

    /**
     * Moves a file from one location to another
     * @param sourcePath source file path
     * @param destinationPath destination file path
     * @return true if file was moved successfully
     */
    public static boolean moveFile(String sourcePath, String destinationPath) {
        try {
            FileUtils.moveFile(new File(sourcePath), new File(destinationPath));
            return true;
        } catch (IOException e) {
            logger.error("Error moving file from {} to {}", sourcePath, destinationPath, e);
            return false;
        }
    }
}
