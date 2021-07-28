package course

import com.soywiz.korge.tiled.TiledMap
import com.soywiz.korma.geom.Point
import com.soywiz.korma.geom.Rectangle

/**
 * A Hitshape is a Polygon with x and y coordinates.
 * @property x The x coordinate.
 * @property y The y coordinate.
 * @property points The [Points][Point] of the polygon, in a List.
 * @constructor Takes the x and y coordinates, plus a list of the polygon points.
 */
class HitShape(var x: Int, var y: Int, var points: List<Point>) {

    /**
     * Returns a List of Points, with the coordinates already applied.
     */
    fun getAllPoints(): List<Point> {
        return points.onEach { it.plus(Point(x, y)) }
    }

    override fun toString(): String {
        return "[$x,$y]:$points"
    }

}

/**
 * Extracts [HitShapes][HitShape] from a [TiledMap].
 * @param tilelayer The tilelayer used for extraction.
 */
fun TiledMap.readHitShapes(tilelayer: Int = 0): List<HitShape> {
    val list = emptyList<HitShape>().toMutableList()
    val tileWidth = this.tilewidth
    val tileHeight = this.tileheight

    data.tilesets.onEach {
        for (j in 0 until this.height) {
            for (i in 0 until this.width) {
                if (it.tiles.firstOrNull { tile -> tile.id == tileLayers[tilelayer][i, j]-1 }?.objectGroup?.objects?.firstOrNull() != null) {
                    val obj = it.tiles.first { tile -> tile.id == tileLayers[tilelayer][i, j]-1 }.objectGroup?.objects?.first()
                    when (obj?.objectShape) {
                        is TiledMap.Object.Shape.Rectangle -> list.add(HitShape(i*tileWidth, j*tileHeight, obj.bounds.toPoints()))
                        is TiledMap.Object.Shape.Polygon -> list.add(HitShape(i*tileWidth, j*tileHeight, (obj.objectShape as TiledMap.Object.Shape.Polygon).points))
                        else -> {}
                    }
                }
            }
        }
    }

    return list
}

/**
 * Scales a list of [Hitshapes][HitShape] to a given scale.
 * @param scale The scale to use.
 */
fun List<HitShape>.scaleTo(scale:Double):List<HitShape>{
    return this.onEach { it.points.onEach { it.mul(scale) } }
}

private fun Rectangle.toPoints(): List<Point> {
    return listOf(
        Point(this.x, this.y),
        Point(this.x + this.width, this.y),
        Point(this.x + this.width, this.y + this.height),
        Point(this.x, this.y + this.height)
    )
}