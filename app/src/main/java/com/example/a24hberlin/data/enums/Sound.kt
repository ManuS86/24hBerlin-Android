package com.example.a24hberlin.data.enums

enum class Sound(val label: String) {
    ALL_STYLES_OF_DARK_MUSIC("All styles of dark music"),
    EIGHTIES("80s"),
    ALTERNATIVE("Alternative"),
    COLD_WAVE("Cold Wave"),
    DARKWAVE("Darkwave"),
    DEATHROCK("Deathrock"),
    DREAM_POP("Dream Pop"),
    EBM("EBM"),
    ELECTRO("Electro"),
    ELECTROCLASH("Electroclash"),
    EXPERIMENTAL("Experimental"),
    FOLK("Folk"),
    GOTHIC_ROCK("Gothic Rock"),
    HARDCORE_PUNK("Hardcore Punk"),
    HI_NRG("Hi-NRG"),
    HORROR_PUNK("Horror Punk"),
    INDIE("Indie"),
    INDUSTRIAL("Industrial"),
    ITALO_DISCO("Italo-Disco"),
    METAL("Metal"),
    MINIMAL("Minimal"),
    MITTELALTER("Mittelalter"),
    NDW("NDW"),
    NEO_FOLK("Neofolk"),
    NEW_BEAT("New Beat"),
    NEW_WAVE("New Wave"),
    POST_PUNK("Post-Punk"),
    POWER_POP("Power Pop"),
    PSYCHOBILLY("Psychobilly"),
    PUNK("Punk"),
    ROCK("Rock"),
    ROCKASBILLY("Rockabilly"),
    SHOEGAZE("Shoegaze"),
    SINGER_SONGWRITER("Singer/Songwriter"),
    SKA("Ska"),
    SYNTH_POP("Synth-Pop"),
    TECHNO("Techno"),
    WAVE("Wave"),
    WORLD_MUSIC("World Music");

    companion object {
        val allValues = entries.toTypedArray()
    }
}