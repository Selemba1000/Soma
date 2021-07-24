import com.soywiz.korge.scene.Scene
import com.soywiz.korge.tiled.readTiledMap
import com.soywiz.korge.tiled.tiledMapView
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.Image
import com.soywiz.korge.view.camera
import com.soywiz.korge.view.position
import com.soywiz.korio.file.std.resourcesVfs
import course.Course
import course.displayCourse

class CourseTestScene: Scene() {

    override suspend fun Container.sceneInit() {
        val course = Course.loadFromFile("courses/testCourse.xml")
        displayCourse(course)
    }
}