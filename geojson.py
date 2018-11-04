import json
import csv
from copy import deepcopy

LATMIN = 51
LATMAX = 51.082
LONMIN = 3.61
LONMAX = 3.80

LAT_STEP = (LATMAX - LATMIN) / 99
LON_STEP = (LONMAX - LONMIN) / 99

template = json.loads('''
{
  "type": "FeatureCollection",
  "features": []
}
''')


feature_template = json.loads('''
{
    "type": "Feature",
    "properties": {},
    "geometry": {
        "type": "Polygon",
        "coordinates": []
    }
}
''')

def color(x):
    return (int(255 * (1 - x)), int(255 * x), 0)

def htmlcolor(x):
    return '#%02x%02x%02x' % color(x)

with open('pollution.csv') as csvfile:
    reader = csv.DictReader(csvfile, fieldnames=["lat", "lon", "pollution"])
    for row in reader:
        feature = deepcopy(feature_template)

        lat = float(row['lat'])
        lon = float(row['lon'])
        pollution = float(row['pollution'])

        # feature['properties']['fill'] = htmlcolor(pollution)
        feature['properties']['pollution'] = pollution
        feature['geometry']['coordinates'] = [[
            [lon, lat],
            [lon, lat + LAT_STEP],
            [lon + LON_STEP, lat + LAT_STEP],
            [lon + LON_STEP, lat],
            [lon, lat],
        ]]

        template['features'].append(feature)

template['features'] = template['features']

with open('pollution.json', 'w') as jsonfile:
    json.dump(template, jsonfile)
