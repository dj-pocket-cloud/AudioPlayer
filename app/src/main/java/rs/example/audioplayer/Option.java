package rs.example.audioplayer;

public class Option {
    private String optionName;
    private String optionDescription;
    private int imgId;

    public Option() {

    }

    public Option(String optionName, String optionDescription, int imgId) {
        this.optionName = optionName;
        this.optionDescription = optionDescription;
        this.imgId = imgId;
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

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

}
