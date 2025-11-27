package authentication;

public class Appointment {
    private int id;
    private Doctor doctor;
    private Patient patient;
    private String date;

    public Appointment(Doctor doctor, String date){
        this.doctor = doctor;
        this.date = date;
    }
    public void setPatient(Patient patient){
        this.patient = patient;
    }
    public int getId(){
        return this.id;
    }
}
