package s3605807.vamshinath.agricultureadvisoryapp.mlhelper



object DiseaseInfoProvider {

    private val info = mapOf(

        // ----------------------------------------------------------
        // APPLE DISEASES
        // ----------------------------------------------------------

        "apple apple scab" to DiseaseInfo(
            name = "Apple Scab",
            symptoms = listOf(
                "Olive-green to black velvety spots on leaves",
                "Distorted, curled leaves",
                "Scabby lesions on fruit"
            ),
            management = listOf(
                "Remove infected leaves and fruit",
                "Improve airflow through pruning",
                "Use resistant varieties"
            ),
            chemical = listOf(
                "Myclobutanil (Rally)",
                "Captan fungicide",
                "Mancozeb spray"
            ),
            organic = listOf(
                "Neem oil weekly sprays",
                "Compost tea foliar spray",
                "Sulfur-based organic fungicide"
            )
        ),

        "apple black rot" to DiseaseInfo(
            name = "Apple Black Rot",
            symptoms = listOf(
                "Purple leaf spots with brown centers",
                "Cankers on branches",
                "Shriveled black fruit (mummies)"
            ),
            management = listOf(
                "Remove infected fruit and branches",
                "Sanitize pruning tools",
                "Improve orchard hygiene"
            ),
            chemical = listOf(
                "Copper oxychloride spray",
                "Thiophanate-methyl fungicide"
            ),
            organic = listOf(
                "Neem oil",
                "Baking soda spray",
                "Remove mummified fruit"
            )
        ),

        "apple cedar apple rust" to DiseaseInfo(
            name = "Cedar Apple Rust",
            symptoms = listOf(
                "Yellow-orange leaf spots",
                "Gelatinous orange galls on cedar trees",
                "Premature leaf drop"
            ),
            management = listOf(
                "Remove cedar trees within 300 meters",
                "Prune infected leaves",
                "Improve air circulation"
            ),
            chemical = listOf(
                "Myclobutanil spray",
                "Propiconazole fungicide"
            ),
            organic = listOf(
                "Sulfur dust",
                "Neem oil",
                "Garlic extract spray"
            )
        ),

        "apple healthy" to DiseaseInfo(
            name = "Healthy Apple Leaf",
            symptoms = listOf("No symptoms."),
            management = listOf("Regular watering and orchard hygiene."),
            chemical = listOf(),
            organic = listOf()
        ),


        // ----------------------------------------------------------
        // BLUEBERRY
        // ----------------------------------------------------------
        "blueberry healthy" to DiseaseInfo(
            name = "Healthy Blueberry Leaf",
            symptoms = listOf("No symptoms."),
            management = listOf("Maintain acidic soil and proper drainage."),
            chemical = listOf(),
            organic = listOf()
        ),


        // ----------------------------------------------------------
        // CHERRY
        // ----------------------------------------------------------
        "cherry including sour powdery mildew" to DiseaseInfo(
            name = "Cherry Powdery Mildew",
            symptoms = listOf(
                "White powder-like growth on leaf surfaces",
                "Leaf distortion",
                "Premature leaf shedding"
            ),
            management = listOf(
                "Prune overcrowded branches",
                "Increase airflow"
            ),
            chemical = listOf(
                "Wettable sulfur",
                "Myclobutanil spray"
            ),
            organic = listOf(
                "Neem oil",
                "Potassium bicarbonate spray",
                "Milk spray (1:10 ratio)"
            )
        ),

        "cherry including sour healthy" to DiseaseInfo(
            name = "Healthy Cherry Leaf",
            symptoms = listOf("No symptoms."),
            management = listOf("Maintain regular pruning."),
            chemical = listOf(),
            organic = listOf()
        ),


        // ----------------------------------------------------------
        // CORN (MAIZE)
        // ----------------------------------------------------------
        "corn maize cercospora leaf spot gray leaf spot" to DiseaseInfo(
            name = "Gray Leaf Spot (Corn)",
            symptoms = listOf(
                "Long narrow tan lesions",
                "Lesions aligned with leaf veins",
                "Reduced plant photosynthesis"
            ),
            management = listOf(
                "Use resistant hybrids",
                "Remove crop debris"
            ),
            chemical = listOf(
                "Strobilurin fungicides",
                "Triazole fungicides"
            ),
            organic = listOf(
                "Neem extract",
                "Compost tea spray"
            )
        ),

        "corn maize common rust" to DiseaseInfo(
            name = "Corn Common Rust",
            symptoms = listOf(
                "Reddish-brown pustules",
                "Scattered lesions",
                "Leaf drying"
            ),
            management = listOf(
                "Use rust-resistant hybrids",
                "Plant early"
            ),
            chemical = listOf(
                "Tebuconazole",
                "Azoxystrobin"
            ),
            organic = listOf(
                "Neem oil",
                "Maintain field sanitation"
            )
        ),

        "corn maize northern leaf blight" to DiseaseInfo(
            name = "Northern Leaf Blight",
            symptoms = listOf(
                "Large cigar-shaped lesions",
                "Lesions expand in cool moist weather"
            ),
            management = listOf(
                "Crop rotation",
                "Remove infected debris"
            ),
            chemical = listOf(
                "Chlorothalonil",
                "Strobilurin fungicides"
            ),
            organic = listOf(
                "Compost tea spray",
                "Improve airflow"
            )
        ),

        "corn maize healthy" to DiseaseInfo(
            name = "Healthy Corn Leaf",
            symptoms = listOf("No symptoms."),
            management = listOf("Maintain fertilizer balance."),
            chemical = listOf(),
            organic = listOf()
        ),


        // ----------------------------------------------------------
        // GRAPE
        // ----------------------------------------------------------
        "grape black rot" to DiseaseInfo(
            name = "Grape Black Rot",
            symptoms = listOf(
                "Brown circular leaf lesions",
                "Black shriveled fruit",
                "Tan lesions on shoots"
            ),
            management = listOf(
                "Remove infected fruit",
                "Improve vineyard airflow"
            ),
            chemical = listOf(
                "Myclobutanil",
                "Ferbam fungicide"
            ),
            organic = listOf(
                "Neem Oil",
                "Sulfur dust"
            )
        ),

        "grape esca black measles" to DiseaseInfo(
            name = "Grape Esca (Black Measles)",
            symptoms = listOf(
                "Tiger-striped leaves",
                "Dark lesions on fruit"
            ),
            management = listOf(
                "Avoid pruning in rain",
                "Remove infected vines"
            ),
            chemical = listOf(
                "Sodium arsenite (restricted)",
                "Boscalid fungicide"
            ),
            organic = listOf(
                "Trichoderma biocontrol",
                "Improve soil health"
            )
        ),

        "grape leaf blight isariopsis leaf spot" to DiseaseInfo(
            name = "Isariopsis Leaf Spot",
            symptoms = listOf(
                "Angular brown leaf lesions",
                "Dark fungal growth under leaves"
            ),
            management = listOf(
                "Prune infected leaves",
                "Improve spacing"
            ),
            chemical = listOf(
                "Copper hydroxide",
                "Mancozeb"
            ),
            organic = listOf(
                "Neem oil spray",
                "Garlic extract"
            )
        ),

        "grape healthy" to DiseaseInfo(
            name = "Healthy Grape Leaf",
            symptoms = listOf("No symptoms."),
            management = listOf("Maintain vineyard hygiene."),
            chemical = listOf(),
            organic = listOf()
        ),


        // ----------------------------------------------------------
        // ORANGE
        // ----------------------------------------------------------
        "orange haunglongbing citrus greening" to DiseaseInfo(
            name = "Citrus Greening (HLB)",
            symptoms = listOf(
                "Blotchy yellow leaf pattern",
                "Lopsided bitter fruit",
                "Twig dieback"
            ),
            management = listOf(
                "Remove infected trees",
                "Control psyllid insects"
            ),
            chemical = listOf(
                "Imidacloprid to control vectors",
                "Oxytetracycline trunk injections"
            ),
            organic = listOf(
                "Neem oil (vector control)",
                "Mulching to improve root health"
            )
        ),


        // ----------------------------------------------------------
        // PEACH
        // ----------------------------------------------------------
        "peach bacterial spot" to DiseaseInfo(
            name = "Peach Bacterial Spot",
            symptoms = listOf(
                "Dark leaf spots",
                "Fruit pitting",
                "Leaf yellowing"
            ),
            management = listOf(
                "Use resistant varieties",
                "Avoid wet foliage"
            ),
            chemical = listOf(
                "Copper hydroxide spray",
                "Oxytetracycline"
            ),
            organic = listOf(
                "Neem oil",
                "Compost tea foliar spray"
            )
        ),

        "peach healthy" to DiseaseInfo(
            name = "Healthy Peach Leaf",
            symptoms = listOf("No disease symptoms."),
            management = listOf("Maintain pruning cycle."),
            chemical = listOf(),
            organic = listOf()
        ),


        // ----------------------------------------------------------
        // PEPPER
        // ----------------------------------------------------------
        "pepper bell bacterial spot" to DiseaseInfo(
            name = "Pepper Bacterial Spot",
            symptoms = listOf(
                "Water-soaked leaf spots",
                "Fruit lesions",
                "Leaf drop"
            ),
            management = listOf(
                "Use disease-free seeds",
                "Avoid touching wet leaves"
            ),
            chemical = listOf(
                "Copper fungicide",
                "Streptomycin (limited use)"
            ),
            organic = listOf(
                "Neem oil",
                "Baking soda spray"
            )
        ),

        "pepper bell healthy" to DiseaseInfo(
            name = "Healthy Pepper Leaf",
            symptoms = listOf("No symptoms."),
            management = listOf("Provide balanced nutrients."),
            chemical = listOf(),
            organic = listOf()
        ),


        // ----------------------------------------------------------
        // POTATO
        // ----------------------------------------------------------
        "potato early blight" to DiseaseInfo(
            name = "Potato Early Blight",
            symptoms = listOf(
                "Brown lesions with concentric rings",
                "Yellowing around spots"
            ),
            management = listOf(
                "Remove affected leaves",
                "Avoid overhead watering"
            ),
            chemical = listOf(
                "Chlorothalonil",
                "Mancozeb"
            ),
            organic = listOf(
                "Neem oil",
                "Compost tea spray"
            )
        ),

        "potato late blight" to DiseaseInfo(
            name = "Potato Late Blight",
            symptoms = listOf(
                "Water-soaked gray lesions",
                "White mold under leaves",
                "Rotting tubers"
            ),
            management = listOf(
                "Destroy infected plants",
                "Improve soil drainage"
            ),
            chemical = listOf(
                "Copper oxychloride",
                "Metalaxyl"
            ),
            organic = listOf(
                "Potassium bicarbonate spray",
                "Mulch to reduce splash spread"
            )
        ),

        "potato healthy" to DiseaseInfo(
            "Healthy Potato Leaf",
            listOf("No disease symptoms."),
            listOf("Maintain soil health."),
            chemical = listOf(),
            organic = listOf()
        ),


        // ----------------------------------------------------------
        // RASPBERRY
        // ----------------------------------------------------------
        "raspberry healthy" to DiseaseInfo(
            name = "Healthy Raspberry Leaf",
            symptoms = listOf("No issues."),
            management = listOf("Regular pruning."),
            chemical = listOf(),
            organic = listOf()
        ),


        // ----------------------------------------------------------
        // SOYBEAN
        // ----------------------------------------------------------
        "soybean healthy" to DiseaseInfo(
            name = "Healthy Soybean Leaf",
            symptoms = listOf("No symptoms."),
            management = listOf("Maintain adequate watering."),
            chemical = listOf(),
            organic = listOf()
        ),


        // ----------------------------------------------------------
        // SQUASH
        // ----------------------------------------------------------
        "squash powdery mildew" to DiseaseInfo(
            name = "Squash Powdery Mildew",
            symptoms = listOf(
                "White powdery fungal growth",
                "Leaf yellowing"
            ),
            management = listOf(
                "Remove severely infected leaves",
                "Improve airflow"
            ),
            chemical = listOf(
                "Myclobutanil",
                "Sulfur fungicide"
            ),
            organic = listOf(
                "Neem oil",
                "Milk spray"
            )
        ),


        // ----------------------------------------------------------
        // STRAWBERRY
        // ----------------------------------------------------------
        "strawberry leaf scorch" to DiseaseInfo(
            name = "Strawberry Leaf Scorch",
            symptoms = listOf(
                "Purple spots with red borders",
                "Leaves dry out"
            ),
            management = listOf(
                "Remove infected leaves",
                "Use drip irrigation"
            ),
            chemical = listOf(
                "Captan fungicide",
                "Mancozeb"
            ),
            organic = listOf(
                "Neem oil",
                "Garlic spray"
            )
        ),

        "strawberry healthy" to DiseaseInfo(
            name = "Healthy Strawberry Leaf",
            symptoms = listOf("No symptoms."),
            management = listOf("Maintain soil moisture."),
            chemical = listOf(),
            organic = listOf()
        ),


        // ----------------------------------------------------------
        // TOMATO (MANY VARIANTS)
        // ----------------------------------------------------------
        "tomato bacterial spot" to DiseaseInfo(
            name = "Tomato Bacterial Spot",
            symptoms = listOf(
                "Small black leaf spots",
                "Greasy lesions",
                "Fruit scarring"
            ),
            management = listOf(
                "Remove infected leaves",
                "Use disease-free seeds"
            ),
            chemical = listOf(
                "Copper hydroxide",
                "Streptomycin (restricted)"
            ),
            organic = listOf(
                "Neem oil",
                "Baking soda solution"
            )
        ),

        "tomato early blight" to DiseaseInfo(
            name = "Tomato Early Blight",
            symptoms = listOf(
                "Dark concentric rings",
                "Yellowing of lower leaves"
            ),
            management = listOf(
                "Remove infected leaves",
                "Improve airflow"
            ),
            chemical = listOf(
                "Chlorothalonil",
                "Mancozeb"
            ),
            organic = listOf(
                "Neem oil",
                "Compost tea"
            )
        ),

        "tomato late blight" to DiseaseInfo(
            name = "Tomato Late Blight",
            symptoms = listOf(
                "Water-soaked gray lesions",
                "White mold under leaf"
            ),
            management = listOf(
                "Destroy infected plants",
                "Avoid wet foliage"
            ),
            chemical = listOf(
                "Copper fungicide",
                "Metalaxyl"
            ),
            organic = listOf(
                "Baking soda spray",
                "Potassium bicarbonate"
            )
        ),

        "tomato leaf mold" to DiseaseInfo(
            name = "Tomato Leaf Mold",
            symptoms = listOf(
                "Yellow upper lesions",
                "Olive-green mold under leaves"
            ),
            management = listOf(
                "Increase airflow",
                "Reduce humidity"
            ),
            chemical = listOf(
                "Chlorothalonil",
                "Copper-based fungicide"
            ),
            organic = listOf(
                "Neem oil",
                "Trichoderma spray"
            )
        ),

        "tomato septoria leaf spot" to DiseaseInfo(
            name = "Tomato Septoria Leaf Spot",
            symptoms = listOf(
                "Small dark circular spots",
                "Black fruiting bodies inside spots"
            ),
            management = listOf(
                "Remove infected leaves",
                "Avoid overhead watering"
            ),
            chemical = listOf(
                "Mancozeb",
                "Chlorothalonil"
            ),
            organic = listOf(
                "Compost tea",
                "Neem oil"
            )
        ),

        "tomato spider mites two spotted spider mite" to DiseaseInfo(
            name = "Tomato Spider Mite",
            symptoms = listOf(
                "Speckled yellowing leaves",
                "Fine webbing",
                "Leaf bronzing"
            ),
            management = listOf(
                "Increase humidity",
                "Wash leaf undersides"
            ),
            chemical = listOf(
                "Abamectin",
                "Spiromesifen"
            ),
            organic = listOf(
                "Neem oil",
                "Soap-water spray"
            )
        ),

        "tomato target spot" to DiseaseInfo(
            name = "Tomato Target Spot",
            symptoms = listOf(
                "Concentric ring lesions",
                "Yellow halos"
            ),
            management = listOf(
                "Remove infected leaves",
                "Provide spacing"
            ),
            chemical = listOf(
                "Chlorothalonil",
                "Mancozeb"
            ),
            organic = listOf(
                "Neem oil",
                "Baking soda solution"
            )
        ),

        "tomato yellow leaf curl virus" to DiseaseInfo(
            name = "Tomato Yellow Leaf Curl Virus",
            symptoms = listOf(
                "Upward curling leaves",
                "Stunted growth"
            ),
            management = listOf(
                "Remove infected plants",
                "Control whiteflies"
            ),
            chemical = listOf(
                "Imidacloprid (whitefly control)",
                "Abamectin"
            ),
            organic = listOf(
                "Sticky yellow traps",
                "Neem oil"
            )
        ),

        "tomato mosaic virus" to DiseaseInfo(
            name = "Tomato Mosaic Virus",
            symptoms = listOf(
                "Mottled leaf patterns",
                "Leaf distortion"
            ),
            management = listOf(
                "Remove infected plants",
                "Sanitize tools"
            ),
            chemical = listOf(
                "No chemical cure (virus)",
                "Use insecticides to control aphids"
            ),
            organic = listOf(
                "Neem oil (vector control)",
                "Use resistant seeds"
            )
        ),

        "tomato healthy" to DiseaseInfo(
            name = "Healthy Tomato Leaf",
            symptoms = listOf("No symptoms."),
            management = listOf("Maintain regular watering and spacing."),
            chemical = listOf(),
            organic = listOf()
        ),


        // ----------------------------------------------------------
        // BACKGROUND (non-leaf)
        // ----------------------------------------------------------
        "background" to DiseaseInfo(
            name = "Not a Leaf",
            symptoms = listOf("The image does not contain a plant leaf."),
            management = listOf("Try placing the leaf inside the scan circle."),
            chemical = listOf(),
            organic = listOf()
        )
    )


    fun getInfo(name: String): DiseaseInfo {

        val normalized = name
            .trim()
            .lowercase()
            .replace("+", " ")
            .replace("_", " ")
            .replace(Regex("\\s+"), " ") // collapse multiple spaces

        return info[normalized] ?: DiseaseInfo(
            name = normalized,
            symptoms = listOf("No information available."),
            management = listOf("No management guidelines available."),
            chemical = emptyList(),
            organic = emptyList()
        )
    }

}



data class DiseaseInfo(
    val name: String,
    val symptoms: List<String>,
    val management: List<String>,
    val chemical: List<String>,
    val organic: List<String>
)


