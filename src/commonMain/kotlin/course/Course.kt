package course

import com.soywiz.korau.sound.PlaybackTimes
import com.soywiz.korau.sound.Sound
import com.soywiz.korau.sound.readMusic
import com.soywiz.korge.tiled.TiledMap
import com.soywiz.korge.tiled.readTiledMap
import com.soywiz.korge.tiled.tiledMapView
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.Image
import com.soywiz.korge.view.position
import com.soywiz.korge.view.solidRect
import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.color.Colors
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korio.serialization.xml.readXml
import com.soywiz.korma.geom.Rectangle

data class Course(
    var timeLimit: Int,
    val tileMap: TiledMap,
    val scrolling: Boolean,
    val music: Sound,
    val bgA: Background,
    val bgB: Background,
    val mapScale: Float,
    val tileBounds: MutableMap<Rectangle, Boolean>,
    val sprites: MutableList<Sprite>
) {

    data class Background(
        val image: Bitmap,
        val xScrollRate: Float,
        val yScrollRate: Float,
        val xOffset: Float,
        val yOffset: Float,
        val scale: Float
    )

    data class Sprite(
        val id: Int,
        val xPos: Double,
        val yPos: Double,
        val settings: Int
    )

    companion object {
        suspend fun loadFromFile(courseFile: String): Course {
            val xml = resourcesVfs[courseFile].readXml()

            val timeLimit = xml.attribute("timeLimit")?.toInt() ?: error("No timeLimit found in xml file!")

            val tileMapFile = xml.attribute("tileMap") ?: error("No tileMap found in xml file area!")
            val scrollingVal = xml.attribute("scrolling") ?: error("No scrolling value found in xml file area!")
            val mapScale = xml.attribute("mapScale")?.toFloat() ?: error("No mapScale value found in xml file area!")
            val musicFile = xml.attribute("music") ?: error("No music file found in xml file area!")

            val tileMap = resourcesVfs[tileMapFile].readTiledMap()
            val scrolling = scrollingVal == "true"
            val music = resourcesVfs[musicFile].readMusic()

            val bgAFile = xml.child("bgA")?.attribute("source") ?: error("No bgAFile file found in bgA!")
            val xScrollRateA =
                xml.child("bgA")?.attribute("xScrollRate")?.toFloat() ?: error("No xScrollRate file found in bgA!")
            val yScrollRateA =
                xml.child("bgA")?.attribute("yScrollRate")?.toFloat() ?: error("No yScrollRate file found in bgA!")
            val xOffsetA =
                xml.child("bgA")?.attribute("xOffset")?.toFloat() ?: error("No xOffset file found in bgA!")
            val yOffsetA =
                xml.child("bgA")?.attribute("yOffset")?.toFloat() ?: error("No yOffset file found in bgA!")
            val scaleA = xml.child("bgA")?.attribute("scale")?.toFloat() ?: error("No scale file found in bgA!")

            val bgA =
                Background(
                    resourcesVfs[bgAFile].readBitmap(),
                    xScrollRateA,
                    yScrollRateA,
                    xOffsetA,
                    yOffsetA,
                    scaleA
                )

            val bgBFile = xml.child("bgB")?.attribute("source") ?: error("No bgBFile file found in bgB!")
            val xScrollRateB =
                xml.child("bgB")?.attribute("xScrollRate")?.toFloat() ?: error("No xScrollRate file found in bgB!")
            val yScrollRateB =
                xml.child("bgB")?.attribute("yScrollRate")?.toFloat() ?: error("No yScrollRate file found in bgB!")
            val xOffsetB =
                xml.child("bgB")?.attribute("xOffset")?.toFloat() ?: error("No xOffset file found in bgB!")
            val yOffsetB =
                xml.child("bgB")?.attribute("yOffset")?.toFloat() ?: error("No yOffset file found in bgB!")
            val scaleB = xml.child("bgB")?.attribute("scale")?.toFloat() ?: error("No scale file found in bgB!")

            val bgB =
                Background(
                    resourcesVfs[bgBFile].readBitmap(),
                    xScrollRateB,
                    yScrollRateB,
                    xOffsetB,
                    yOffsetB,
                    scaleB
                )

            //add boundings and sprites
            val boundings: MutableMap<Rectangle, Boolean> = mutableMapOf()
            val sprites: MutableList<Sprite> = mutableListOf()
            tileMap.objectLayers.filter { it.name == "Boundings" }.forEach { boundingLayer ->
                boundingLayer.objectsById.forEach { obj ->
                    val b = obj.value.bounds
                    val solid = obj.value.properties.get("solid")?.string?.toInt()
                    boundings[Rectangle(b.x * mapScale, b.y * mapScale, b.width * mapScale, b.height * mapScale)] = solid == 1
                }
            }

            tileMap.objectLayers.filter { it.name == "Sprites" }.forEach { spriteLayer ->
                spriteLayer.objectsById.forEach { obj ->
                    val id = obj.value.properties.get("id")?.string?.toInt()
                    val settings = obj.value.properties.get("settings")?.string?.toInt()
                    if (id == null) error("sprites must all have a custom int property named 'id' ")
                    if (settings == null) error("sprites must all have a custom int property named 'settings' ")
                    val sprite = Sprite(id, obj.value.bounds.x * mapScale, obj.value.bounds.y * mapScale, settings)
                    sprites.add(sprite)
                }
            }

            return Course(timeLimit, tileMap, scrolling, music, bgA, bgB, mapScale, boundings, sprites)
        }
    }

    suspend fun loadIntoScene(parent: Container) {
        parent.addChild(Image(bgB.image).apply { position(bgB.xOffset, bgB.yOffset); scale = bgB.scale.toDouble() })
        parent.addChild(Image(bgA.image).apply { position(bgA.xOffset, bgA.yOffset); scale = bgA.scale.toDouble() })
        parent.tiledMapView(tileMap).apply { scale = mapScale.toDouble() }
        //TODO:
            //load timelimit-graphics and logic
            //add scrolling
            //load TileBounds
            //debugging:
            tileBounds.forEach {
                if (it.value) parent.solidRect(it.key.width, it.key.height, Colors.RED).apply { position(it.key.x, it.key.y) }
                else if (!it.value) parent.solidRect(it.key.width, it.key.height, Colors.YELLOW).apply { position(it.key.x, it.key.y) }
            }
            //load Sprites
            //debugging:
            sprites.forEach {
                parent.solidRect(10, 10, Colors.GREEN).apply { position(it.xPos, it.yPos) }
            }

        music.play(PlaybackTimes.INFINITE)
    }
}

suspend fun Container.displayCourse(course: Course) {
    course.loadIntoScene(this)
}
