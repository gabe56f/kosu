package hu.gabe.kosu.objects.api.beatmap

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.serializer

typealias Mods = @Serializable(with = ModsSerializer::class) List<Mod>
typealias ModsAcronym = @Serializable(with = ModsAcronymSerializer::class) List<Mod>

@Serializable(with = ModSerializer::class)
enum class Mod(
    internal val acronym: String,
    internal val incompatibleMods: List<String> = emptyList(),
    internal val bitwiseShift: Int? = null
) {
    EASY("EZ", listOf("HR"), bitwiseShift = 1),
    NO_FAIL("NF", listOf("SD", "PF", "AP", "RX"), bitwiseShift = 0),
    HALF_TIME("HT", listOf("DT", "NC"), bitwiseShift = 8),
    DAYCORE("DC"),
    HARD_ROCK("HR", listOf("EZ", "MR"), bitwiseShift = 4),
    SUDDEN_DEATH("SD", listOf("NF", "PF", "RX", "AP"), bitwiseShift = 5),
    PERFECT("PF", listOf("NF", "SD", "RX"), bitwiseShift = 14),
    DOUBLE_TIME("DT", listOf("HT"), bitwiseShift = 6),
    NIGHTCORE("NC", listOf("HT"), bitwiseShift = 9),
    HIDDEN("HD", bitwiseShift = 3),
    FLASHLIGHT("FL", bitwiseShift = 10),
    BLINDS("BL"),
    STRICT_TRACKING("ST"),
    TARGET("TP"),
    DIFFICULTY_ADJUST("DA"),
    CLASSIC("CL"),
    RANDOM("RD"),
    MIRROR("MR", listOf("HR"), bitwiseShift = 30),
    ALTERNATE("AL"),
    SINGLE_TAP("SG"),
    AUTOPLAY("AT"),
    CINEMA("CN"),
    RELAX("RX", bitwiseShift = 7),
    AUTOPILOT("AP", listOf("NF", "SD", "PF", "RX", "SO"), bitwiseShift = 13),
    SPUN_OUT("SO", listOf("AP"), bitwiseShift = 12),
    TRANSFORM("TR"),
    WIGGLE("WG"),
    SPIN_IN("SI"),
    GROW("GR"),
    DEFLATE("DF"),
    WIND_UP("WU"),
    WIND_DOWN("WD"),
    TRACEABLE("TC"),
    BARREL_ROLL("BR"),
    APPROACH_DIFFERENT("AD"),
    MUTED("MU"),
    NO_SCOPE("NS"),
    MAGNETISED("MG"),
    REPEL("RP"),
    ADAPTIVE_SPEED("AS"),
    FREEZE_FRAME("FR"),
    TOUCH_DEVICE("TD", bitwiseShift = 2),
    SWAP("SW"),
    FLOATING_FRUITS("FF"),
    FADE_IN("FI", listOf("HD", "FL"), bitwiseShift = 20),
    FOUR_KEYS("4K", bitwiseShift = 15),
    FIVE_KEYS("5K", bitwiseShift = 16),
    SIX_KEYS("6K", bitwiseShift = 17),
    SEVEN_KEYS("7K", bitwiseShift = 18),
    EIGHT_KEYS("8K", bitwiseShift = 19),
    NINE_KEYS("9K", bitwiseShift = 24),
    TEN_KEYS("10K"),
    ONE_KEY("1K"),
    TWO_KEYS("2K"),
    THREE_KEYS("3K"),
    DUAL_STAGES("DS"),
    INVERT("IN"),
    CONSTANT_SPEED("CS"),
    HOLD_OFF("HO"),
    ACCURACY_CHALLENGE("AC"),
    BLOOM("BM"),
    NO_RELEASE("NR"),
    DEPTH("DP"),
    COVER("CO");
}

@Serializable
data class ModObject(val acronym: String)

object ModSerializer : KSerializer<Mod> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("hu.gabe.objects.Mod", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Mod) {
        encoder.encodeString(value.acronym)
    }

    override fun deserialize(decoder: Decoder): Mod {
        val value = decoder.decodeString()
        return Mod.entries.first { it.acronym == value }
    }
}

fun List<Mod>?.toIntFlag(): Long? = if (this != null) {
    var output = 0L
    forEach {
        if (it.bitwiseShift != null)
            output = output or (1L shl it.bitwiseShift)
    }
    output
} else null

object ModsAcronymSerializer : KSerializer<List<Mod>> {
    private val modSerializer = ListSerializer(serializer<String>())
    override val descriptor: SerialDescriptor =
        SerialDescriptor("hu.gabe.objects.ModsAcronym", modSerializer.descriptor)

    override fun deserialize(decoder: Decoder): List<Mod> {
        var value = decoder.decodeSerializableValue(modSerializer).joinToString("") { it }
        if (value.isEmpty()) return emptyList()

        val mods = ArrayList<Mod>(4)
        if ("10K" in value) {
            mods.add(Mod.TEN_KEYS)
            value = value.replace("10K", "")
        }
        if (value.isEmpty()) return mods

        for (acronym in value.chunked(2))
            Mod.entries.firstOrNull { it.acronym == acronym }?.let { mods.add(it) }
        return mods
    }

    override fun serialize(
        encoder: Encoder,
        value: List<Mod>
    ) {
        encoder.encodeString(value.joinToString("") { it.acronym })
    }
}

object ModsSerializer : KSerializer<List<Mod>> {
    private val modSerializer = ListSerializer(serializer<ModObject>())
    private val modListSerializer = ListSerializer(ModSerializer)
    override val descriptor: SerialDescriptor = SerialDescriptor("hu.gabe.objects.Mods", modListSerializer.descriptor)

    override fun serialize(encoder: Encoder, value: List<Mod>) {
        encoder.encodeString(value.joinToString("") { it.acronym })
    }

    override fun deserialize(decoder: Decoder): List<Mod> {
        var value = decoder.decodeSerializableValue(modSerializer).joinToString("") { it.acronym }
        if (value.isEmpty()) return emptyList()

        val mods = ArrayList<Mod>(4)
        if ("10K" in value) {
            mods.add(Mod.TEN_KEYS)
            value = value.replace("10K", "")
        }
        if (value.isEmpty()) return mods

        for (acronym in value.chunked(2))
            Mod.entries.firstOrNull { it.acronym == acronym }?.let { mods.add(it) }
        return mods
    }
}