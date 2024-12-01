package il.ac.kinneret.mjmay.sendingacks;

import java.util.HashMap;
import java.util.Scanner;

/**
 * This program calculates the total time required to transmit a file over a network
 * using specified packet size, acknowledgment size, bandwidth, and round-trip time (RTT).
 * The user provides inputs through a command line format, and the program processes and calculates the result.
 * 
 * @author [Your Name]
 * @version 1.0
 */
public class SendingWithACKsJava {

    /**
     * The main method reads input parameters from the user, calculates the transmission time, and outputs the result.
     * It validates inputs and uses helper methods to perform specific parsing and calculations.
     * 
     * @param args Command-line arguments (not used in this implementation).
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("java -jar SendingWithAcksJava-5784.jar -filesize=f -packetsize=p -acksize=a -bandwidth=b -rtt=r");
            String inputLine = scanner.nextLine();

            HashMap<String, String> inputs = parseInputs(inputLine);

            double filesize = parseSize(inputs.get("filesize"));
            double packetsize = parseSize(inputs.getOrDefault("packetsize", "1KB")); // Default packet size
            double acksize = parseSize(inputs.getOrDefault("acksize", "50B"));      // Default ACK size
            double bandwidth = parseBandwidth(inputs.get("bandwidth"));
            double rtt = parseRTT(inputs.get("rtt"));

            double transmitTime = (packetsize * 8) / bandwidth;
            double ackTime = (acksize * 8) / bandwidth;
            long totalPackets = (long) Math.ceil(filesize / packetsize);
            double totalTime = totalPackets * (rtt + transmitTime + ackTime);

            System.out.printf("Output: %.6f s%n", totalTime);
        } catch (IllegalArgumentException e) {
            System.err.println("Missing parameter. Expected output:");
            printUsage();
        } finally {
            scanner.close();
        }
    }

    /**
     * Parses the input line into a map of parameter names and values.
     * 
     * @param inputLine The input string containing parameters in the format `-key=value`.
     * @return A map containing the parameter names as keys and their values.
     * @throws IllegalArgumentException if required parameters are missing.
     */
    private static HashMap<String, String> parseInputs(String inputLine) {
        HashMap<String, String> inputs = new HashMap<>();
        String[] parts = inputLine.split(" ");

        for (String part : parts) {
            if (part.startsWith("-") && part.contains("=")) {
                String[] keyValue = part.substring(1).split("=");
                if (keyValue.length == 2) {
                    inputs.put(keyValue[0].toLowerCase(), keyValue[1].toLowerCase());
                }
            }
        }

        if (!inputs.containsKey("filesize") || !inputs.containsKey("bandwidth") || !inputs.containsKey("rtt")) {
            throw new IllegalArgumentException("Missing required parameters.");
        }
        return inputs;
    }

    /**
     * Parses a size string (e.g., "1KB") and converts it to bytes.
     * 
     * @param size The size string with units (B, KB, MB, GB).
     * @return The size in bytes.
     * @throws IllegalArgumentException if the size format is invalid or cannot be parsed.
     */
    private static double parseSize(String size) {
        size = size.trim().toLowerCase();
        try {
            if (size.endsWith("kb") || size.endsWith("k")) {
                return Double.parseDouble(size.replace("kb", "").replace("k", "")) * Math.pow(2, 10);
            } else if (size.endsWith("mb") || size.endsWith("m")) {
                return Double.parseDouble(size.replace("mb", "").replace("m", "")) * Math.pow(2, 20);
            } else if (size.endsWith("gb") || size.endsWith("g")) {
                return Double.parseDouble(size.replace("gb", "").replace("g", "")) * Math.pow(2, 30);
            } else if (size.endsWith("b")) {
                return Double.parseDouble(size.replace("b", ""));
            } else {
                throw new IllegalArgumentException("Invalid size format: " + size);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Failed to parse size: " + size + ". Ensure it contains a valid number.");
        }
    }

    /**
     * Parses the bandwidth string (e.g., "100Mbps") and converts it to bits per second.
     * 
     * @param bandwidth The bandwidth string with units (Mbps, Gbps).
     * @return The bandwidth in bits per second.
     * @throws IllegalArgumentException if the bandwidth format is invalid.
     */
    private static double parseBandwidth(String bandwidth) {
        bandwidth = bandwidth.trim().toLowerCase();
        if (bandwidth.endsWith("mbps")) {
            return Double.parseDouble(bandwidth.replace("mbps", "")) * Math.pow(10, 6);
        } else if (bandwidth.endsWith("gbps")) {
            return Double.parseDouble(bandwidth.replace("gbps", "")) * Math.pow(10, 9);
        } else {
            throw new IllegalArgumentException("Invalid bandwidth format: " + bandwidth);
        }
    }

    /**
     * Parses the RTT string (e.g., "100ms") and converts it to seconds.
     * 
     * @param rtt The RTT string with unit (ms).
     * @return The RTT in seconds.
     * @throws IllegalArgumentException if the RTT format is invalid.
     */
    private static double parseRTT(String rtt) {
        rtt = rtt.trim().toLowerCase();
        if (rtt.endsWith("ms")) {
            return Double.parseDouble(rtt.replace("ms", "")) / 1000.0; // Convert ms to seconds
        } else {
            throw new IllegalArgumentException("Invalid RTT format: " + rtt);
        }
    }

    /**
     * Prints the usage instructions for the program.
     */
    private static void printUsage() {
        System.out.println("Usage: -filesize=f -packetsize=p -acksize=a -bandwidth=b -rtt=r");
        System.out.println("f can be of units B, KB, MB, GB");
        System.out.println("p can be of units B, KB");
        System.out.println("a can be of unit B");
        System.out.println("b can be of units mbps, gbps");
        System.out.println("r can be of unit ms");
    }
}
