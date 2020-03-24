from io import StringIO
from typing import Tuple

from geo_cli.model.uls_data_element_definition import UlsDataElementDefinition


class UlsRecordFormat:
    AN = None
    EN = None
    LO = None

    def __init__(self, data_element_definitions: Tuple[UlsDataElementDefinition, ...]):
        self.data_element_definitions = data_element_definitions

    @classmethod
    def parse(cls, string: str):
        data_element_definitions = []
        for line in StringIO(string):
            line = line.strip()
            if not line:
                continue
            line_split = line.split(sep=None, maxsplit=1)
            if not line_split:
                continue
            position = int(line_split[0])
            definition_split = line_split[1].rsplit(sep=None, maxsplit=1)
            data_element_definitions.append(UlsDataElementDefinition(name=definition_split[0], position=position, type_=definition_split[1]))
        return UlsRecordFormat(data_element_definitions=tuple(data_element_definitions))

    @property
    def record_type(self) -> str:
        assert self.data_element_definitions[0].name.startswith("Record Type")
        record_type = self.data_element_definitions[0].name.rsplit(sep=None, maxsplit=1)[-1].lstrip("[").rstrip("]")
        return record_type


# Definitions copied from https://www.fcc.gov/sites/default/files/public_access_database_definitions_v3.pdf
UlsRecordFormat.AN = UlsRecordFormat.parse("""
1 Record Type [AN] char(2)
2 Unique System Identifier numeric(9,0)
3 ULS File Number char(14)
4 EBF Number varchar(30)
5 Call Sign char(10)
6 Antenna Action Performed char(1)
7 Antenna Number integer
8 Location Number integer
9 Receive Zone Code char(6)
10 Antenna Type Code char(1)
11 Height to Tip numeric(5,1)
12 Height to Center RAAT numeric(5,1)
13 Antenna Make varchar(25)
14 Antenna Model varchar(25)
15 Tilt numeric(3,1)
16 Polarization Code char(5)
17 Beamwidth numeric(4,1)
18 Gain numeric(4,1)
19 Azimuth numeric(4,1)
20 Height Above Avg Terrain numeric(5,1)
21 Diversity Height numeric(5,1)
22 Diversity Gain numeric(4,1)
23 Diversity Beam numeric(4,1)
24 Reflector Height numeric(5,1)
25 Reflector Width numeric(4,1)
26 Reflector Separation numeric(5,1)
27 Passive Repeater Number integer
28 Back‐to‐Back Tx Dish Gain numeric(4,1)
29 Back‐to‐Back Rx Dish Gain numeric(4,1)
30 Location Name varchar(20)
31 Passive Repeater Sequence ID integer
32 Alternative CGSA Method char(1)
33 Path Number integer
34 Line loss numeric(3,1)
35 Status Code char(1)
36 Status Date mm/dd/yyyy
37 PSD/Non‐PSD Methodology varchar(10)
38 Maximum ERP numeric(15,3)""")

UlsRecordFormat.EN = UlsRecordFormat.parse("""\
1 Record Type [EN] char(2)
2 Unique System Identifier numeric(9,0)
3 ULS File Number char(14)
4 EBF Number varchar(30)
5 Call Sign char(10)
6 Entity Type char(2)
7 Licensee ID char(9)
8 Entity Name varchar(200)
9 First Name varchar(20)
10 MI char(1)
11 Last Name varchar(20)
12 Suffix char(3)
13 Phone char(10)
14 Fax char(10)
15 Email varchar(50)
16 Street Address varchar(60)
17 City varchar(20)
18 State char(2)
19 Zip Code char(9)
20 PO Box varchar(20)
21 Attention Line varchar(35)
22 SGIN char(3)
23 FCC Registration Number (FRN) char(10)
24 Applicant Type Code char(1)
25 Applicant Type Code Other char(40)
26 Status Code char(1)
27 Status Date mm/dd/yyyy""")

UlsRecordFormat.LO = UlsRecordFormat.parse("""\
1 Record Type [LO] char(2)
2 Unique System Identifier numeric(9,0)
3 ULS File Number char(14)
4 EBF Number varchar(30)
5 Call Sign char(10)
6 Location Action Performed char(1)
7 Location Type Code char(1)
8 Location Class Code char(1)
9 Location Number integer
10 Site Status char(1)
11 Corresponding Fixed Location integer
12 Location Address varchar(80)
13 Location City char(20)
14 Location County/Borough/Parish varchar(60)
15 Location State char(2)
16 Radius of Operation numeric(5,1)
17 Area of Operation Code char(1)
18 Clearance Indicator char(1)
19 Ground Elevation numeric(7,1)
20 Latitude Degrees integer
21 Latitude Minutes integer
22 Latitude Seconds numeric(3,1)
23 Latitude Direction char(1)
24 Longitude Degrees integer
25 Longitude Minutes integer
26 LongitudeSeconds numeric(3,1)
27 Longitude Direction char(1)
28 Max Latitude Degrees integer
29 Max Latitude Minutes integer
30 Max Latitude Seconds numeric(3,1)
31 Max Latitude Direction char(1)
32 Max Longitude Degrees integer
33 Max Longitude Minutes integer
34 Max Longitude Seconds numeric(3,1)
35 Max Longitude Direction char(1)
36 Nepa char(1)
37 Quiet Zone Notification Date mm/dd/yyyy
38 Tower Registration Number char(10)
39 Height of Support Structure numeric(7,1)
40 Overall Height of Structure numeric(7,1)
41 Structure Type char(7)
42 Airport ID char(4)
43 Location Name char(20)
44 Units Hand Held integer
45 Units Mobile integer
46 Units Temp Fixed integer
47 Units Aircraft integer
48 Units Itinerant integer
49 Status Code char(1)
50 Status Date mm/dd/yyyy
51 Earth Station Agreement char(1)""")
