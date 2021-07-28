package Scenes

import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.Image
import com.soywiz.korge.view.addUpdater
import com.soywiz.korge.view.position
import com.soywiz.korim.format.readBitmapSlice
import com.soywiz.korio.file.std.resourcesVfs
import kotlinx.coroutines.launch
import quality_of_life_functions.newSound

class IntroScene : Scene() {
    override suspend fun Container.sceneInit(){

        val StartSound = newSound("Gameboy_Startup_Screen.mp3")

        val logo = Image(resourcesVfs["Logos/DOD_logo_pixel_w.png"].readBitmapSlice()).apply {
            position(GameModule.size.width / 2.0 - 256, GameModule.size.height / 2.0 -1024)
            scale = 1.0
        }
        addChild(logo)

        var tmp = true

        logo.addUpdater {
            if(logo.y <= GameModule.size.height/2.0 - 256){
                y += 3
            }
            else if(tmp){
                tmp = false
                launch {
                    StartSound.playAndWait()
                    sceneContainer.changeTo<StartScene>()
                }
            }
        }
    }
}