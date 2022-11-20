package mis055.assignment.enums


enum class Country(val shortName: String) {

    // european union countries
    AT("Austria"),
    BE("Belgium"),
    BG("Bulgaria"),
    HR("Croatia"),
    CY("Cyprus"),
    CZ("Czechia"),
    DK("Denmark"),
    EE("Estonia"),
    FI("Finland"),
    FR("France"),
    DE("Germany"),
    GR("Greece"),
    HU("Hungary"),
    IE("Ireland"),
    IT("Italy"),
    LV("Latvia"),
    LT("Lithuania"),
    LU("Luxembourg"),
    MT("Malta"),
    NL("Netherlands"),
    PL("Poland"),
    PT("Portugal"),
    RO("Romania"),
    SK("Slovakia"),
    SI("Slovenia"),
    ES("Spain"),
    SE("Sweden"),

    // arabian countries
    AE("United Arab Emirates"),
    QA("Qatar"),
    BH("Bahrain");

    companion object {
        fun valueOfShortName(shortName: String): Country =
            values().find { it.shortName == shortName }
                ?: throw IllegalArgumentException("Unknown country: '${shortName}'")
    }
}
