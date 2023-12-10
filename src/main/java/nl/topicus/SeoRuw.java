package nl.topicus;

public record SeoRuw(Long id, boolean isOk) implements Registratie {
    public String wazzaa(){
        return "oeps";
    }
}
