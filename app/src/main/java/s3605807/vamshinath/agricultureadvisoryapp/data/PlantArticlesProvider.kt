package s3605807.vamshinath.agricultureadvisoryapp.data

data class PlantArticle(
    val id: Int,
    val title: String,
    val description: String,
    val content: String
)


object PlantArticlesProvider {

    val articles = listOf(
        PlantArticle(
            id = 1,
            title = "How to Prevent Fungal Diseases in Plants",
            description = "Simple steps to reduce fungal infections like blight, mildew, and rot.",
            content = """
                • Ensure proper plant spacing to improve air circulation.
                • Avoid overhead watering and water at soil level.
                • Remove infected leaves immediately.
                • Apply preventive fungicides during humid seasons.
                • Keep the soil well drained to avoid excess moisture.
            """.trimIndent()
        ),
        PlantArticle(
            id = 2,
            title = "Top 10 Tips to Improve Plant Immunity",
            description = "Strengthen plant natural defenses with soil care and nutrients.",
            content = """
                • Add compost and organic matter regularly.
                • Maintain proper pH for every plant type.
                • Use seaweed extract or humic acid.
                • Avoid waterlogging and root rot.
                • Add beneficial microbes to soil.
            """.trimIndent()
        ),
        PlantArticle(
            id = 3,
            title = "How to Control Insect Pests Naturally",
            description = "Natural ways to prevent insects like aphids, mites, and whiteflies.",
            content = """
                • Use neem oil spray weekly.
                • Install yellow sticky traps for whiteflies.
                • Grow companion plants like marigold.
                • Release ladybugs or lacewings.
                • Remove weeds near the garden area.
            """.trimIndent()
        ),
        PlantArticle(
            id = 4,
            title = "Watering Mistakes That Cause Plant Diseases",
            description = "Overwatering is the #1 cause of fungal and bacterial infections.",
            content = """
                • Water early in the morning, not at night.
                • Avoid constant moisture in soil.
                • Ensure pots have drainage holes.
                • Mulch to prevent soil splash.
                • Never leave leaves wet for long.
            """.trimIndent()
        ),
        PlantArticle(
            id = 5,
            title = "Preventing Soil-Borne Diseases",
            description = "Soil health is key for preventing root rot and wilt diseases.",
            content = """
                • Rotate crops every season.
                • Solarize soil to kill pathogens.
                • Add Trichoderma-based biofungicides.
                • Remove infected roots immediately.
                • Avoid reusing contaminated soil.
            """.trimIndent()
        ),
        PlantArticle(
            id = 6,
            title = "Improving Airflow to Reduce Leaf Diseases",
            description = "Poor airflow leads to blight and mildew. Here’s how to fix it.",
            content = """
                • Prune dense foliage.
                • Maintain clean surroundings.
                • Provide adequate spacing while planting.
                • Use fans in indoor plant setups.
                • Avoid overcrowding in pots and gardens.
            """.trimIndent()
        ),
        PlantArticle(
            id = 7,
            title = "Organic Treatments for Common Plant Diseases",
            description = "Natural cures for mildew, blight, rust, spot diseases.",
            content = """
                • Neem oil for mildew and rust.
                • Baking soda spray for fungal diseases.
                • Compost tea for immunity.
                • Garlic extract as broad-spectrum treatment.
                • Milk spray for powdery mildew.
            """.trimIndent()
        ),
        PlantArticle(
            id = 8,
            title = "How to Prevent Viral Plant Diseases",
            description = "Viruses spread quickly—prevention is better than cure.",
            content = """
                • Control whiteflies and aphids (virus carriers).
                • Remove infected plants immediately.
                • Sanitize your gardening tools.
                • Use disease-resistant varieties.
                • Avoid handling plants when wet.
            """.trimIndent()
        ),
        PlantArticle(
            id = 9,
            title = "Top Mulching Techniques to Protect Plants",
            description = "Mulching stops soil splash, conserves water, and reduces diseases.",
            content = """
                • Use straw, dry leaves, or wood chips.
                • Avoid mulch touching plant stems.
                • Reapply every season.
                • Mulch helps regulate soil temperature.
                • Prevents fungal spores from reaching leaves.
            """.trimIndent()
        ),
        PlantArticle(
            id = 10,
            title = "Beginners Guide: Preventing Plant Diseases",
            description = "Simple prevention habits every gardener must follow.",
            content = """
                • Water wisely and avoid overwatering.
                • Remove dead plant material regularly.
                • Use clean pots and sterilized tools.
                • Add organic matter to soil.
                • Observe plants daily to catch diseases early.
            """.trimIndent()
        )
    )
}
