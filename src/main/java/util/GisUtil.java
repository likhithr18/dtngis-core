package util;

import com.vividsolutions.jts.geom.*;
import constants.Constants;
import gis.GISCoordinate;
import gis.GisFeature;
import gis.Properties;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains several utility methods related to GIS features
 * Created by arka on 2/5/17.
 */
public class GisUtil {

    /**
     * Create a JTS Point from GisFeature
     * @param feature
     * @return
     */
    private static Point createPoint(GisFeature feature) {
        GeometryFactory geometryFactory = new GeometryFactory();

        Coordinate coordinate = new Coordinate(feature.getCoordinates().get(0).getX(),
                feature.getCoordinates().get(0).getY());
        Point point = geometryFactory.createPoint(coordinate);

        return point;
    }

    /**
     * Create a JTS Linestring from GisFeature
     * @param feature
     * @return
     */
    private static LineString createLineString(GisFeature feature) {
        GeometryFactory geometryFactory = new GeometryFactory();

        List<GISCoordinate> featureCoordinates = feature.getCoordinates();
        Coordinate[] coordinates = new Coordinate[featureCoordinates.size()];
        int i = 0;
        for(GISCoordinate featureCoordinate:featureCoordinates) {
            coordinates[i] = new Coordinate(featureCoordinate.getX(), featureCoordinate.getY());
        }

        LineString lineString = geometryFactory.createLineString(coordinates);

        return lineString;
    }

    /**
     * Create a JTS Polygon from GisFeature
     * @param feature
     * @return
     */
    private static Polygon createPolygon(GisFeature feature) {
        GeometryFactory geometryFactory = new GeometryFactory();

        List<GISCoordinate> featureCoordinates = feature.getCoordinates();
        Coordinate[] coordinates = new Coordinate[featureCoordinates.size()];
        int i = 0;
        for(GISCoordinate featureCoordinate:featureCoordinates) {
            coordinates[i] = new Coordinate(featureCoordinate.getX(), featureCoordinate.getY());
        }

        Polygon polygon = geometryFactory.createPolygon(coordinates);

        return polygon;
    }

    /**
     * Create GisFeature object from Geometry of JTS
     * @param finalPoint
     * @param gisGeometryType
     * @param properties
     * @return
     */
    private static GisFeature createGisFeature(Geometry finalPoint, String gisGeometryType, Properties properties) {
        String source = "MERGE";
        String destination = "MERGE";
        String timestamp = "MERGE";
        String createLat = "MERGE";
        String createLon = "MERGE";
        String geometryType = gisGeometryType;

        Coordinate[] coordinates = finalPoint.getCoordinates();
        List<GISCoordinate> gisCoordinates = new ArrayList<GISCoordinate>();
        for(Coordinate coordinate:coordinates) {
            gisCoordinates.add(new GISCoordinate(coordinate.x, coordinate.y));
        }

        return new GisFeature(source, timestamp, destination, createLat, createLon,
                geometryType, gisCoordinates, properties);
    }

    /**
     * Returns the union of two Gis features
     * @param feature1
     * @param feature2
     * @return
     */
    public static GisFeature union(GisFeature feature1, GisFeature feature2) {
        if(feature1.getGeometryType().equals(Constants.GIS_GEOMETRY_TYPE_POLYGON) &&
                feature2.getGeometryType().equals(Constants.GIS_GEOMETRY_TYPE_POLYGON)) {
            return polygonUnion(feature1, feature2);
        }

        if(feature1.getGeometryType().equals(Constants.GIS_GEOMETRY_TYPE_LINESTRING) &&
                feature2.getGeometryType().equals(Constants.GIS_GEOMETRY_TYPE_LINESTRING)) {
            return  linestringUnion(feature1,feature2);
        }

        if(feature1.getGeometryType().equals(Constants.GIS_GEOMETRY_TYPE_POINT) &&
                feature2.getGeometryType().equals(Constants.GIS_GEOMETRY_TYPE_POINT)) {
            return pointUnion(feature1, feature2);
        }

        return null;
    }

    private static GisFeature polygonUnion(GisFeature feature1, GisFeature feature2) {
        Polygon polygon1 = createPolygon(feature1);
        Polygon polygon2 = createPolygon(feature2);

        Geometry finalPolygon = polygon1.union(polygon2);

        return createGisFeature(finalPolygon, Constants.GIS_GEOMETRY_TYPE_POLYGON, feature1.getProperties());
    }

    private static GisFeature linestringUnion(GisFeature feature1, GisFeature feature2) {
        LineString lineString1 = createLineString(feature1);
        LineString lineString2 = createLineString(feature2);

        Geometry finalLineString = lineString1.union(lineString2);

        return createGisFeature(finalLineString, Constants.GIS_GEOMETRY_TYPE_LINESTRING, feature1.getProperties());
    }

    private static GisFeature pointUnion(GisFeature feature1, GisFeature feature2) {
        Point point1 = createPoint(feature1);
        Point point2 = createPoint(feature2);

        Geometry finalPoint = point1.union(point2);

        return createGisFeature(finalPoint, Constants.GIS_GEOMETRY_TYPE_POINT, feature1.getProperties());
    }

    /**
     * Returns the intersection of two Gis features
     * @param feature1
     * @param feature2
     * @return
     */
    public static GisFeature intersection(GisFeature feature1, GisFeature feature2) {
        if(feature1.getGeometryType().equals(Constants.GIS_GEOMETRY_TYPE_POLYGON) &&
                feature2.getGeometryType().equals(Constants.GIS_GEOMETRY_TYPE_POLYGON)) {
            return polygonIntersection(feature1, feature2);
        }

        if(feature1.getGeometryType().equals(Constants.GIS_GEOMETRY_TYPE_LINESTRING) &&
                feature2.getGeometryType().equals(Constants.GIS_GEOMETRY_TYPE_LINESTRING)) {
            return  linestringIntersection(feature1,feature2);
        }

        if(feature1.getGeometryType().equals(Constants.GIS_GEOMETRY_TYPE_POINT) &&
                feature2.getGeometryType().equals(Constants.GIS_GEOMETRY_TYPE_POINT)) {
            return pointIntersection(feature1, feature2);
        }

        return null;
    }

    private static GisFeature polygonIntersection(GisFeature feature1, GisFeature feature2) {
        Polygon polygon1 = createPolygon(feature1);
        Polygon polygon2 = createPolygon(feature2);

        Geometry finalPolygon = polygon1.intersection(polygon2);

        return createGisFeature(finalPolygon, Constants.GIS_GEOMETRY_TYPE_POLYGON, feature1.getProperties());
    }

    private static GisFeature linestringIntersection(GisFeature feature1, GisFeature feature2) {
        LineString lineString1 = createLineString(feature1);
        LineString lineString2 = createLineString(feature2);

        Geometry finalLineString = lineString1.intersection(lineString2);

        return createGisFeature(finalLineString, Constants.GIS_GEOMETRY_TYPE_LINESTRING, feature1.getProperties());
    }

    private static GisFeature pointIntersection(GisFeature feature1, GisFeature feature2) {
        Point point1 = createPoint(feature1);
        Point point2 = createPoint(feature2);

        Geometry finalPoint = point1.intersection(point2);

        return createGisFeature(finalPoint, Constants.GIS_GEOMETRY_TYPE_POINT, feature1.getProperties());
    }

    /**
     * Returns the buffer Feature for the given feature
     * @param feature1
     * @return
     */
    public static GisFeature bufferFeature(GisFeature feature1) {
        return null;
    }
}
