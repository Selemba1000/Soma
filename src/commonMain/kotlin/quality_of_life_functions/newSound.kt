package quality_of_life_functions

import com.soywiz.korau.sound.readSound
import com.soywiz.korio.file.std.resourcesVfs

suspend fun newSound(fileName: String) = resourcesVfs["sounds/$fileName"].readSound()