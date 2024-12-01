package il.ac.kinneret.mjmay.sendingacks;

import java.util.HashMap;

/**
 * This program calculates the total time required to transmit a file over a network
 * using specified packet size, acknowledgment size, bandwidth, and round-trip time (RTT).
 * 
 * @author [Your Name]
 * @version 1.0
 */
public class SendingWithACKsJava {

    /**
     * The main method reads input parameters, calculates the transmission time, and outputs the result.
     * 
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        try {
            HashMap<String, String> inputs = parseInputs(args);

            double filesize = parseSize(inputs.get("filesize"));
            double packetsize = parseSize(inputs.get("packetsize"));
            double acksize = parseSize(inputs.get("acksize"));
            double bandwidth = parseBandwidth(inputs.get("bandwidth"));
            double rtt = parseRTT(inputs.get("rtt"));

            // Calculate transmission time
            double transmitTime = (packetsize * 8) / bandwidth;
            double ackTime = (acksize * 8) / bandwidth;
            long totalPackets = (long) Math.ceil(filesize / packetsize);
            double totalTime = totalPackets * (rtt + transmitTime + ackTime);

            // Print the result
            System.out.printf("Output: %.6f s%n", totalTime);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            printUsage();
        }
    }

    /**
     * Parses the input arguments into a map of parameter names and values.
     * 
     * @param args Command-line arguments.
     * @return A map containing parameter names and values.
     * @throws IllegalArgumentException if required parameters are missing.
     */
    private static HashMap<String, String> parseInputs(String[] args) {
        HashMap<String, String> inputs = new HashMap<>();

        for (String arg : args) {
            if (arg.startsWith("-") && arg.contains("=")) {
                String[] keyValue = arg.substring(1).split("=");
                if (keyValue.length == 2) {
                    inputs.put(keyValue[0].toLowerCase(), keyValue[1].toLowerCase());
                }
            }
        }

        if (!inputs.containsKey("filesize") || !inputs.containsKey("packetsize") ||
            !inputs.containsKey("acksize") || !inputs.containsKey("bandwidth") ||
            !inputs.containsKey("rtt")) {
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
            if (size.endsWith("kb")) {
                return Double.parseDouble(size.replace("kb", "")) * Math.pow(2, 10);
            } else if (size.endsWith("mb")) {
                return Double.parseDouble(size.replace("mb", "")) * Math.pow(2, 20);
            } else if (size.endsWith("gb")) {
                return Double.parseDouble(size.replace("gb", "")) * Math.pow(2, 30);
            } else if (size.endsWith("b")) {
                return Double.parseDouble(size.replace("b", ""));
            } else {
                throw new IllegalArgumentException("Invalid size format: " + size);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Failed to parse size: " + size);
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
        try {
            if (bandwidth.endsWith("mbps")) {
                return Double.parseDouble(bandwidth.replace("mbps", "")) * Math.pow(10, 6);
            } else if (bandwidth.endsWith("gbps")) {
                return Double.parseDouble(bandwidth.replace("gbps", "")) * Math.pow(10, 9);
            } else {
                throw new IllegalArgumentException("Invalid bandwidth format: " + bandwidth);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Failed to parse bandwidth: " + bandwidth);
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
        try {
            if (rtt.endsWith("ms")) {
                return Double.parseDouble(rtt.replace("ms", "")) / 1000.0;
            } else {
                throw new IllegalArgumentException("Invalid RTT format: " + rtt);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Failed to parse RTT: " + rtt);
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
