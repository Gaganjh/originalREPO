package com.manulife.pension.ps.web.tpadownload;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.manulife.pension.ps.service.report.tpadownload.dao.TedFTPHelper;
import com.manulife.pension.ps.service.report.tpadownload.dao.TedFTPServer;

/**
 * @author Mark Eldridge
 * 
 *         This servlet will handle downloading current and history files by compressing them and
 *         sending them.
 */
public class TedFileDownloadServlet extends HttpServlet {

    public static final Logger logger = Logger.getLogger(TedFileDownloadServlet.class);

    public static final String REQUESTED_TED_FILES = "requestedTedFiles";

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     * 
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     * 
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * @param request HttpServletRequest object
     * @param response HttpServletResponse object
     */
    public void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> processRequest");
        }
        TedRequestedFiles files = (TedRequestedFiles) request.getAttribute(REQUESTED_TED_FILES);
        if (files != null && files.getFileListing().size() > 0) {
            response.setContentType("application/octet-stream");
            if (files.getFileListing().size() > 1) {
                response.setHeader("Content-Disposition",
                        "attachment; filename=\"" + files.getZipFileName() + "\"");
                compressFiles(response, files);
            } else if (files.getFileListing().size() == 1) {
                TedRequestedFile singleFile = (TedRequestedFile) files.getFileListing().get(0);
                String fileLocationDirectory = files.getDirectory();
                response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); 
    	        response.setHeader("Pragma", "no-cache");
                sendSingleFile(response, fileLocationDirectory, singleFile);
            }
        	response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); 
	        response.setHeader("Pragma", "no-cache");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("exit <- processRequest");
        }
    }

    public static void compressFiles(HttpServletResponse response, TedRequestedFiles requestedFiles) {
        ZipOutputStream out = null;
        TedFTPHelper ftpServer = null;

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> compressFiles");
        }

        try {
            String fileLocationDirectory = requestedFiles.getDirectory();
            ArrayList fileListing = requestedFiles.getFileListing();

            ftpServer = new TedFTPHelper(Boolean.parseBoolean(TedFTPServer.getTedFtpInd()));
            ftpServer.connect();

            if (logger.isDebugEnabled()) {
                logger.debug("Successfully connected to FTP Server");
            }

            CheckedOutputStream csum = new CheckedOutputStream(response.getOutputStream(),
                    new Adler32());
            out = new ZipOutputStream(new BufferedOutputStream(csum));

            if (logger.isDebugEnabled()) {
                logger.debug("Compressing files...");
            }

            for (int i = 0; i < fileListing.size(); i++) {
                // Get filename and name it as a ZIP in the compressed file, if
                // it was a BAK in the
                // FTP Server
                // That way the the user can unzip it without having to rename
                // it.

                TedRequestedFile fileToSend = (TedRequestedFile) fileListing.get(i);
                StringBuffer filename = new StringBuffer(fileToSend.getFilename().substring(0,
                        fileToSend.getFilename().indexOf(".")));
                if (fileToSend.getYearEndInd() != null)
                    filename.append((fileToSend.getYearEndInd().equals("Y") ? "Y" : ""));
                filename.append(".ZIP");
                String fullPath = fileLocationDirectory + fileToSend.getContractNumber();
                out.putNextEntry(new ZipEntry(filename.toString()));

                // Stream the file from the FTP Server into the ZIP Compression
                // output stream.
                boolean status = ftpServer.getFile(fullPath, fileToSend.getFilename(), out);
                if (!status) {
                    logger.error("TED FTP ERROR: Unable to find file: " + fullPath + "\\"
                            + filename);
                }
            }
            if (logger.isDebugEnabled()) {
                logger.debug("Finished Compressing files...");
            }

        } catch (SocketException e) {
            if (logger.isDebugEnabled()) {
                logger.debug("User cancelled download before compression completed...");
            }
        } catch (Throwable e) {
            logger.error("ERROR Compressing TED Files: ", e);
        } finally {
            try {
                ftpServer.disconnect();
            } catch (Exception e) {
            }
            try {
                out.close();
            } catch (Exception e) {
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("exit <- compressFiles");
        }
    }

    private void sendSingleFile(HttpServletResponse response, String fileLocationDirectory,
            TedRequestedFile singleFile) {
        TedFTPHelper ftpServer = null;

        if (logger.isDebugEnabled()) {
            logger.debug("enter -> sendSingleFile");
        }
        String fullPath = "";
        String filename = "";
        String contract = "";
        String yearEndInd = "";

        if (singleFile != null && !singleFile.equals("")) {
            filename = singleFile.getFilename();
            contract = singleFile.getContractNumber();
            yearEndInd = singleFile.getYearEndInd();
        }
        StringBuffer singleFileName = new StringBuffer(filename.substring(0, filename.indexOf(".")));
        if (yearEndInd != null)
            singleFileName.append((yearEndInd.equals("Y") ? "Y" : ""));
        singleFileName.append(".ZIP");

        response.setHeader("Content-Disposition",
                "attachment; filename=\"" + singleFileName.toString() + "\"");
        fullPath = fileLocationDirectory + contract;
        try {
            ftpServer = new TedFTPHelper(Boolean.parseBoolean(TedFTPServer.getTedFtpInd()));
            ftpServer.connect();

            boolean status = ftpServer.getFile(fullPath, filename, response.getOutputStream());

            if (!status) {
                logger.error("Error transfering TED File from FTP Stream to Response OutputStream: "
                        + fullPath + "\\" + filename);
            }

        } catch (Throwable t) {
            logger.error("ERROR sending TED File: Contract=" + contract + " File Path=" + fullPath,
                    t);
        } finally {
            try {
                ftpServer.disconnect();
            } catch (Exception e) {
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("exit <- sendSingleFile");
        }
    }

}
