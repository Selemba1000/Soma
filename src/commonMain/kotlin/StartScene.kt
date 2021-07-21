package Scenes

import com.soywiz.korge.input.onClick
import com.soywiz.korge.input.onOut
import com.soywiz.korge.input.onOver
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.ui.UIButton
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.Image
import com.soywiz.korge.view.position
import com.soywiz.korim.format.readBitmapSlice
import com.soywiz.korio.file.std.resourcesVfs

class StartScene : Scene() {
    override suspend fun Container.sceneInit() {

        val background = Image(resourcesVfs["Backgrounds/Mainscreen.jpg"].readBitmapSlice(), 0.0)
        addChild(background)

        val startbutton = Image(resourcesVfs["Buttons/play_unpressed.png"].readBitmapSlice()).apply {
            position(GameModule.size.width / 2.0 - 144, GameModule.size.height / 2.0 - 196)
            scale = 0.3
            onOver { bitmap = resourcesVfs["Buttons/play_hover.png"].readBitmapSlice()}
            onOut { bitmap = resourcesVfs["Buttons/play_unpressed.png"].readBitmapSlice() }
            onClick{
                bitmap = resourcesVfs["Buttons/play_pressed.png"].readBitmapSlice()
                sceneContainer.changeTo<IntroScene>()
            }
        }
        addChild(startbutton)

        val creditsbutton = Image(resourcesVfs["Buttons/credits_unpressed.png"].readBitmapSlice()).apply {
            position(GameModule.size.width / 2.0 - 144, GameModule.size.height / 2.0 - 48)
            scale = 0.3
            onOver { bitmap = resourcesVfs["Buttons/credits_hover.png"].readBitmapSlice()}
            onOut { bitmap = resourcesVfs["Buttons/credits_unpressed.png"].readBitmapSlice() }
            onClick{
                bitmap = resourcesVfs["Buttons/play_pressed.png"].readBitmapSlice()
                sceneContainer.changeTo<IntroScene>()
            }
        }
        addChild(creditsbutton)

        val exitbutton  = Image(resourcesVfs["Buttons/exit_unpressed.png"].readBitmapSlice()).apply {
            position(GameModule.size.width / 2.0 - 144, GameModule.size.height / 2.0 + 100)
            scale = 0.3
            onOver { bitmap = resourcesVfs["Buttons/exit_hover.png"].readBitmapSlice()}
            onOut { bitmap = resourcesVfs["Buttons/exit_unpressed.png"].readBitmapSlice() }
            onClick{
                bitmap = resourcesVfs["Buttons/exit_pressed.png"].readBitmapSlice()
                views.gameWindow.close()
            }
        }
        addChild(exitbutton)


    }

}