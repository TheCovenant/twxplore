package models

import java.util.Date

import edu.rpi.tw.twks.uri.Uri
import io.github.tetherlessworld.twxplore.lib.geo.models.domain.UnparsedGeometry
import models.domain.{Feature, FeatureType, FrequencyRange, TimestampRange}

object GeoTestData {

    // WKT contained by the featureGeometry
    val containedWkt = "POLYGON((22.43151320189357 30.24760210803391,24.80456007689357 30.24760210803391,24.80456007689357 28.408717322168872,22.43151320189357 28.408717322168872,22.43151320189357 30.24760210803391))"
    val containedGeometry = UnparsedGeometry(label = None, uri = Uri.parse("http://example.com/containedGeometry"), wkt = containedWkt)
    val containedFeature = Feature(geometry = containedGeometry.parse().get, label = Some("Contained feature"), `type` = Some(FeatureType.MilitaryInstallation), uri = Uri.parse("http://example.com/containedFeature"))

    // WKT containing the featureGeometry
    val containingWkt = "POLYGON((4.345703124999991 41.66202547963382,44.77539062499999 41.66202547963382,44.77539062499999 5.994966023841324,4.345703124999991 5.994966023841324,4.345703124999991 41.66202547963382))"
    val containingGeometry = UnparsedGeometry(label = None, uri = Uri.parse("http://example.com/containingGeometry"), wkt = containingWkt)
    val containingFeature = Feature(geometry = containingGeometry.parse().get, label = Some("Containing feature"), `type` = Some(FeatureType.State), uri = Uri.parse("http://example.com/containingFeature"))

    val featureGeometry = UnparsedGeometry(
        label = Some("Test feature geometry"),
        wkt = "POLYGON ((30 10, 40 40, 20 40, 10 20, 30 10))",
        uri = Uri.parse("http://example.com/geometry"))
    val feature = Feature(
        frequency = Some(1.0 * 1000000),
        geometry = featureGeometry.parse().get,
        label = Some("Test feature"),
        locality = Some("Troy"),
        regions = List("New York"),
        postalCode = Some("12180"),
        timestamp = Some(new Date(2020 - 1900, 2, 13, 16, 32, 0)), // Use an exact date rather than the current date, because serialization loses sub-seconds.
        transmissionPower = Some(30),
        `type` = Some(FeatureType.MetropolitanDivision),
        uri = Uri.parse("http://example.com/feature")
    )
    val featureWithRanges = Feature(
        frequencyRange = Some(FrequencyRange(1.0 * 1000000, 2.0 * 1000000)),
        geometry = featureGeometry.parse().get,
        label = Some("Test feature"),
        locality = Some("Troy"),
        regions = List("New York"),
        postalCode = Some("12180"),
        timestampRange = Some(TimestampRange(new Date(2020 - 1900, 2, 13, 16, 32, 0), new Date(2020 - 1900, 2, 14, 16, 32, 0))),
        transmissionPower = Some(30),
        `type` = Some(FeatureType.MetropolitanDivision),
        uri = Uri.parse("http://example.com/featureWithRanges")
    )

}
