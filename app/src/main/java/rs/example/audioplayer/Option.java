package rs.example.audioplayer;

public class Option {
    private String optionName;
    private String optionDescription;

    public Option() {

    }

    public Option(String optionName, String optionDescription) {
        this.optionName = optionName;
        this.optionDescription = optionDescription;
    }

    public String getOptionName() {
        return optionName;
    };

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public String getOptionDescription() {
        return optionDescription;
    };

    public void setOptionDescription(String optionDescription) {
        this.optionDescription = optionDescription;
    }
}
