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
    fun getPoints(): List<Point> {
        return points.onEach { it.plus(Point(x, y)) }
    }

}

/**
 * Extracts [HitShapes][HitShape] from a [TiledMap].
 * @param tilelayer The tilelayer used for extraction.
 */
fun TiledMap.readHitShapes(tilelayer: Int = 0): List<HitShape> {
    val list = emptyList<HitShape>().toMutableList()

    data.tilesets.onEach {
        for (j in 0..this.height) {
            for (i in 0..this.width) {
                if (it.tiles[tileLayers[tilelayer][i, j]].objectGroup?.objects?.firstOrNull() != null) {
                    val obj = it.tiles[tileLayers[tilelayer][i, j]].objectGroup?.objects?.first()
                    when (obj?.objectShape) {
                        is TiledMap.Object.Shape.Rectangle -> list.add(HitShape(i, j, obj.bounds.toPoints()))
                        is TiledMap.Object.Shape.Polygon -> list.add(HitShape(i, j, (obj.objectShape as TiledMap.Object.Shape.Polygon).points))
                        else -> {}
                    }
                }
            }
        }
    }

    return list
}

private fun Rectangle.toPoints(): List<Point> {
    return listOf(
        Point(this.x, this.y),
        Point(this.x + this.width, this.y),
        Point(this.x + this.width, this.y + this.height),
        Point(this.x, this.y + this.height)
    )
}