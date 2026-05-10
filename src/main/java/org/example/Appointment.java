package org.example;

import java.time.*;
import java.util.*;

import static org.example.Constants.*;

/**
 * Represents an appointment between a user and an administrator.
 * Stores appointment details and manages available time slots.
 */
public class Appointment {

	private User user;
	private Administrator admin;
	private LocalDate date;
	private LocalTime startTime;
	private int duration;
	private AppointmentType type;
	private AppointmentStatus status;
	private int maxParticipants;

	/**
	 * Creates an appointment object.
	 *
	 * @param user appointment owner
	 * @param admin related administrator
	 * @param date appointment date
	 * @param startTime appointment start time
	 * @param duration appointment duration in minutes
	 * @param type appointment type
	 * @param status appointment status
	 */
	public Appointment(User user, Administrator admin, LocalDate date, LocalTime startTime, int duration, AppointmentType type, AppointmentStatus status) {
		this.user = user;
		this.admin = admin;
		this.date = date;
		this.startTime = startTime;
		this.duration = duration;
		this.type = type;
		this.status = status;

		switch (this.type) {
			case URGENT, INDIVIDUAL: maxParticipants = 1; break;
			case VIRTUAL: maxParticipants = 5; break;
			default: maxParticipants = 3;
		}
	}

	/**
	 * @return appointment user
	 */
	public User getUser() { return user; }

	/**
	 * @return related administrator
	 */
	public Administrator getAdmin() {
		return admin;
	}

	/**
	 * @return appointment date
	 */
	public LocalDate getDate() {
		return date;
	}

	/**
	 * @return appointment start time
	 */
	public LocalTime getStartTime() {
		return startTime;
	}

	/**
	 * @return appointment duration in minutes
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * @return appointment type
	 */
	public AppointmentType getType() {
		return type;
	}

	/**
	 * @return appointment status
	 */
	public AppointmentStatus getStatus() {
		return status;
	}

	/**
	 * @return maximum allowed participants
	 */
	public int getMaxParticipants() {
		return maxParticipants;
	}

	/**
	 * Sets appointment date.
	 *
	 * @param date new appointment date
	 */
	public void setDate(LocalDate date) { this.date = date; }

	/**
	 * Sets appointment start time.
	 *
	 * @param startTime new start time
	 */
	public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

	/**
	 * Sets appointment duration.
	 *
	 * @param duration new duration in minutes
	 */
	public void setDuration(int duration) { this.duration = duration; }

	/**
	 * Sets appointment status.
	 *
	 * @param status new appointment status
	 */
	public void setStatus(AppointmentStatus status) { this.status = status; }

	/**
	 * Sets appointment type and updates participant limit.
	 *
	 * @param type new appointment type
	 */
	public void setType(AppointmentType type) {
		this.type = type;

		switch (this.type) {
			case URGENT, INDIVIDUAL: maxParticipants = 1; break;
			case VIRTUAL: maxParticipants = 5; break;
			default: maxParticipants = 3;
		}
	}

	/**
	 * Checks available time slots for an administrator on a specific date.
	 *
	 * @param date target date
	 * @param adminUsername administrator username
	 * @param duration requested appointment duration
	 * @return array representing available time slots
	 */
	public static boolean[] availableTimeSlots(LocalDate date, String adminUsername, int duration){
		boolean[] available = new boolean[12];
		Arrays.fill(available, true);

		List <Appointment> appointments = JsonHandler.loadList(APPOINTMENTS_FILE, Appointment.class);

		for (int i = 0; i < appointments.size(); i++){
			if (appointments.get(i).getAdmin().getUsername().equals(adminUsername) && appointments.get(i).getDate().equals(date) && appointments.get(i).getStatus() != AppointmentStatus.CANCELLED){
				int hour = appointments.get(i).getStartTime().getHour();
				int minute = appointments.get(i).getStartTime().getMinute();
				int index = (hour - 9) * 2 + (minute / 30);

				available[index] = false;

				if (appointments.get(i).getDuration() == 60 && index < 11)
					available[index + 1] = false;

				if (duration == 60 && index > 0)
					available[index - 1] = false;
			}
		}

		return available;
	}

	/**
	 * Empty constructor required for JSON.
	 */
	public Appointment() {}
}