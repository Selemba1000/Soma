import Scenes.IntroScene
import Scenes.StartScene
import com.soywiz.korge.scene.Module
import com.soywiz.korge.scene.Scene
import com.soywiz.korinject.AsyncInjector
import com.soywiz.korma.geom.Anchor
import com.soywiz.korma.geom.ScaleMode
import com.soywiz.korma.geom.SizeInt
import kotlin.reflect.KClass

object GameModule : Module() {

    override val clipBorders: Boolean = false
    override val scaleAnchor: Anchor = Anchor.MIDDLE_CENTER
    override val scaleMode: ScaleMode = ScaleMode.EXACT
    override val mainScene: KClass<out Scene> = IntroScene::class
    override val size: SizeInt = SizeInt(1920,1080)
    override val icon: String = "Logos/TaskBarLogo.png"
    override val title: String = "SOMA"

    override suspend fun AsyncInjector.configure() {
        mapPrototype { IntroScene() }
        mapPrototype { StartScene() }

        //views().gameWindow.fullscreen = true

    }
}