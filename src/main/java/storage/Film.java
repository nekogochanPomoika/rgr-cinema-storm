package storage;

public class Film {
    public int id;
    public String url;
    public String name;
    public String year;
    public String country;
    public String category;
    public String directors;
    public String mainActors;
    public String description;
    public String englishName;
    public String trailerUrl;
    public String imageUrl;
    public String tagline;
    public int budget;
    public int fees;
    public String premiereDate;
    public int ageLimit;
    public String duration;
    public String screenwriters;
    public String producers;
    public String operators;
    public String composers;
    public String artists;
    public String editors;
    public String otherActors;
    public float rating;
    public int marksCount;

    public String toJSONString() {
        return '{' +
                "  \"url\":\"" + url + '\"' +
                ", \"id\":\"" + id + '\"' +
                ", \"imageUrl\":\"" + imageUrl + '\"' +
                ", \"trailerUrl\":\"" + trailerUrl + '\"' +
                ", \"name\":\"" + name + '\"' +
                ", \"description\":\"" + description + '\"' +
                ", \"rating\":\"" + rating + '\"' +
                ", \"marksCount\":\"" + marksCount + '\"' +

                ", \"fields\":" +
                "{ \"englishName\":\"" + englishName + '\"' +
                ", \"year\":\"" + year + '\"' +
                ", \"country\":\"" + country + '\"' +
                ", \"category\":\"" + category + '\"' +
                ", \"directors\":\"" + directors + '\"' +
                ", \"mainActors\":\"" + mainActors + '\"' +
                ", \"tagline\":\"" + tagline + '\"' +
                ", \"budget\":\"" + budget + '\"' +
                ", \"fees\":\"" + fees + '\"' +
                ", \"premiereDate\":\"" + premiereDate + '\"' +
                ", \"ageLimit\":\"" + ageLimit + '\"' +
                ", \"duration\":\"" + duration + '\"' +
                ", \"screenwriters\":\"" + screenwriters + '\"' +
                ", \"producers\":\"" + producers + '\"' +
                ", \"operators\":\"" + operators + '\"' +
                ", \"composers\":\"" + composers + '\"' +
                ", \"artists\":\"" + artists + '\"' +
                ", \"editors\":\"" + editors + '\"' +
                ", \"otherActors\":\"" + otherActors + '\"' + '}' +
                '}';
    }
}
