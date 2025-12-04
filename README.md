# Medical Appointment Management System

A Java-based application for managing medical appointments between patients and doctors. The system provides appointment scheduling, filtering, and management functionalities for both user types.

---

## 🚀 Features

### 👨‍⚕️ Doctor Features
- Create available appointment slots
- View all their appointments
- Filter appointments by:
  - Date range
  - Patient name
  - Appointment status
- Mark an appointment as **completed**
- Cancel an appointment
- Make a scheduled appointment available again
- Delete an appointment

### 🧑 Patient Features
- View all their appointments and filter by:
    - Date range
    - Appointment Status
- Search available appointments using:
  - Date range
  - Specialization
- Book an appointment
- Cancel an appointment

### 🌐 Shared Features (Patient & Doctor)
- View appointments
- Filter appointments

---

## 🧱 Core Classes

### **Doctor**
Key methods:
- `createAvailableSlot(LocalDateTime dateTime, String notes)`
- `completeAppointment(Appointment appointment)`
- `cancelAppointment(Appointment appointment)`
- `getMyAppointments()`
- `getMyAppointments(start, end, patientFullName, status)`
- `makeAppointmentAvailable(Appointment appointment)`
- `deleteAppointment(Appointment appointment)`

### **Patient**
Key methods:
- `bookAppointment(Appointment appointment)`
- `cancelAppointment(Appointment appointment)`
- `getMyAppointments()`
- `searchAvailableAppointments(from, to, doctorId, specialization)`

---

## 🗂 Project Structure (Simplified)

```
src/
├── users/
│   ├── Doctor.java
│   ├── Patient.java
│   └── User.java
├── appointments/
│   └── Appointment.java
└── database_management/
    ├── AppointmentService.java
    ├── DoctorService.java
    └── PatientService.java
```

---

## ⚙️ Technologies Used
- **Java**
- **JavaFX** 
- **Supabase**
- **LocalDateTime** 

---

## 🛠 How It Works

### Appointment Lifecycle
1. Doctor creates available slots.
2. Patient searches and books an available appointment.
3. Doctor completes, cancels, or re-opens an appointment.
4. Both users can view and filter appointments.

### Appointment Status Values
- `AVAILABLE`
- `SCHEDULED`
- `COMPLETED`
- `CANCELLED`

---

## 📚 Database Layer

Handled through:
- `SupabaseClient`
- `AppointmentService`
- `DoctorService`
- `PatientService`

These services manage:
- CRUD operations
- Appointment filtering
- Status updates

---

## ▶️ Running the Project

### Compile
```bash
javac -d out $(find src -name "*.java")
```

### Run
```bash
java -cp out Main
```

Or (if using Maven):
```bash
mvn clean install
mvn javafx:run
```
---
Doctor
jlancer@gmail.com
password: abc

Patient:
jsmith@gmail.com
password: 123
---

## 📝 Future Improvements
- Advanced appointment filtering for patients
- UI (JavaFX or web interface)
- Email/SMS notifications
- Authentication upgrades
- REST API support
