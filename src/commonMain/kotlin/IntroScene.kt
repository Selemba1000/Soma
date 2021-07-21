package Scenes

import com.soywiz.korau.sound.PlaybackTimes
import com.soywiz.korau.sound.Sound
import com.soywiz.korau.sound.readSound
import com.soywiz.korev.Key
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.Image
import com.soywiz.korge.view.addUpdater
import com.soywiz.korge.view.position
import com.soywiz.korim.format.readBitmapSlice
import com.soywiz.korio.file.Vfs
import com.soywiz.korio.file.VfsFile
import com.soywiz.korio.file.std.resourcesVfs
import kotlinx.coroutines.launch

class IntroScene : Scene() {
    override suspend fun Container.sceneInit(){

        val logo = Image(resourcesVfs["Logos/SOMA_Logo_w.png"].readBitmapSlice()).apply {
            position(GameModule.size.width / 2.0 - 256, GameModule.size.height / 2.0 -1024)
            scale = 0.53
        }
        addChild(logo)

        var tmp = 0

        logo.addUpdater {
            if(logo.y <= GameModule.size.height/2.0 - 256){
                y += 3
            }else {
                if (tmp==120) {
                    launch { sceneContainer.changeTo<StartScene>()}
                }
                tmp += 1
            }
        }
    }
}