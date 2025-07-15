package com.ssandeep79.springseleniumdemo.demo.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Locale;

/**
 * Utility class for date and time operations
 * Provides methods for formatting, parsing, and manipulating dates and times
 */
public class DateTimeUtility {

    /**
     * Formats a date in the specified pattern
     * @param date the LocalDate to format
     * @param pattern the date pattern
     * @return formatted date string
     */
    public static String formatDate(LocalDate date, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return date.format(formatter);
    }

    /**
     * Formats a date in the specified pattern and locale
     * @param date the LocalDate to format
     * @param pattern the date pattern
     * @param locale the locale for formatting
     * @return formatted date string
     */
    public static String formatDate(LocalDate date, String pattern, Locale locale) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern, locale);
        return date.format(formatter);
    }

    /**
     * Parses a date string using the given pattern
     * @param dateStr the date string to parse
     * @param pattern the pattern to use for parsing
     * @return the parsed LocalDate
     * @throws DateTimeParseException if the date string cannot be parsed
     */
    public static LocalDate parseDate(String dateStr, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDate.parse(dateStr, formatter);
    }

    /**
     * Parses a date string using the given pattern and locale
     * @param dateStr the date string to parse
     * @param pattern the pattern to use for parsing
     * @param locale the locale for parsing
     * @return the parsed LocalDate
     * @throws DateTimeParseException if the date string cannot be parsed
     */
    public static LocalDate parseDate(String dateStr, String pattern, Locale locale) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern, locale);
        return LocalDate.parse(dateStr, formatter);
    }

    /**
     * Gets the current date in a specific timezone
     * @param zoneId the timezone ID
     * @return LocalDate in the specified timezone
     */
    public static LocalDate getCurrentDateInTimezone(String zoneId) {
        return LocalDate.now(ZoneId.of(zoneId));
    }

    /**
     * Gets the current date and time in a specific timezone
     * @param zoneId the timezone ID
     * @return LocalDateTime in the specified timezone
     */
    public static LocalDateTime getCurrentDateTimeInTimezone(String zoneId) {
        return LocalDateTime.now(ZoneId.of(zoneId));
    }

    /**
     * Converts LocalDateTime from one timezone to another
     * @param dateTime the LocalDateTime to convert
     * @param fromZoneId source timezone ID
     * @param toZoneId target timezone ID
     * @return converted LocalDateTime
     */
    public static LocalDateTime convertTimezone(LocalDateTime dateTime, String fromZoneId, String toZoneId) {
        ZonedDateTime sourceZonedDateTime = dateTime.atZone(ZoneId.of(fromZoneId));
        ZonedDateTime targetZonedDateTime = sourceZonedDateTime.withZoneSameInstant(ZoneId.of(toZoneId));
        return targetZonedDateTime.toLocalDateTime();
    }

    /**
     * Calculates the difference between two dates in days
     * @param startDate the start date
     * @param endDate the end date
     * @return number of days between dates
     */
    public static long getDaysBetween(LocalDate startDate, LocalDate endDate) {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    /**
     * Calculates the difference between two timestamps in milliseconds
     * @param startDateTime the start date and time
     * @param endDateTime the end date and time
     * @return difference in milliseconds
     */
    public static long getMillisBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return ChronoUnit.MILLIS.between(startDateTime, endDateTime);
    }

    /**
     * Adds specified days to a date
     * @param date the source date
     * @param days number of days to add (can be negative)
     * @return the resulting date
     */
    public static LocalDate addDays(LocalDate date, int days) {
        return date.plusDays(days);
    }

    /**
     * Gets the first day of the month for a given date
     * @param date the source date
     * @return first day of the month
     */
    public static LocalDate getFirstDayOfMonth(LocalDate date) {
        return date.with(TemporalAdjusters.firstDayOfMonth());
    }

    /**
     * Gets the last day of the month for a given date
     * @param date the source date
     * @return last day of the month
     */
    public static LocalDate getLastDayOfMonth(LocalDate date) {
        return date.with(TemporalAdjusters.lastDayOfMonth());
    }

    /**
     * Checks if a year is a leap year
     * @param year the year to check
     * @return true if leap year
     */
    public static boolean isLeapYear(int year) {
        return Year.isLeap(year);
    }

    /**
     * Gets date with a specific day of week in the same week
     * @param date the source date
     * @param dayOfWeek the target day of week
     * @return date adjusted to the specified day of week
     */
    public static LocalDate withDayOfWeek(LocalDate date, DayOfWeek dayOfWeek) {
        return date.with(TemporalAdjusters.nextOrSame(dayOfWeek));
    }

    /**
     * Creates a timestamp string for file naming in format yyyyMMdd_HHmmss
     * @return formatted timestamp string
     */
    public static String getTimestampForFilename() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
    }

    /**
     * Checks if a date is between two other dates (inclusive)
     * @param date the date to check
     * @param startDate the start date
     * @param endDate the end date
     * @return true if date is between startDate and endDate (inclusive)
     */
    public static boolean isDateBetween(LocalDate date, LocalDate startDate, LocalDate endDate) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    /**
     * Gets a date that is the specified number of business days from the start date
     * @param startDate the starting date
     * @param businessDays the number of business days to add
     * @return the resulting date after adding business days
     */
    public static LocalDate addBusinessDays(LocalDate startDate, int businessDays) {
        LocalDate result = startDate;
        int addedDays = 0;
        
        while (addedDays < businessDays) {
            result = result.plusDays(1);
            if (!(result.getDayOfWeek() == DayOfWeek.SATURDAY || 
                  result.getDayOfWeek() == DayOfWeek.SUNDAY)) {
                addedDays++;
            }
        }
        
        return result;
    }

    /**
     * Gets the current Unix timestamp in seconds
     * @return Unix timestamp in seconds
     */
    public static long getCurrentUnixTimestamp() {
        return Instant.now().getEpochSecond();
    }

    /**
     * Converts a Unix timestamp to LocalDateTime in system default timezone
     * @param unixTimestamp the Unix timestamp in seconds
     * @return LocalDateTime equivalent
     */
    public static LocalDateTime unixTimestampToLocalDateTime(long unixTimestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(unixTimestamp), 
                                       ZoneId.systemDefault());
    }

    /**
     * Formats duration in a human-readable format (e.g., "2h 30m 45s")
     * @param seconds total duration in seconds
     * @return human-readable duration string
     */
    public static String formatDuration(long seconds) {
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long remainingSeconds = seconds % 60;
        
        StringBuilder result = new StringBuilder();
        
        if (hours > 0) {
            result.append(hours).append("h ");
        }
        if (hours > 0 || minutes > 0) {
            result.append(minutes).append("m ");
        }
        result.append(remainingSeconds).append("s");
        
        return result.toString();
    }

    /**
     * Gets age in years from a birth date
     * @param birthDate the birth date
     * @return age in years
     */
    public static int getAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    /**
     * Checks if a date is a weekend
     * @param date the date to check
     * @return true if date is a weekend (Saturday or Sunday)
     */
    public static boolean isWeekend(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    /**
     * Gets the quarter of the year for a date
     * @param date the date to check
     * @return quarter (1-4)
     */
    public static int getQuarter(LocalDate date) {
        return (date.getMonthValue() - 1) / 3 + 1;
    }
}
