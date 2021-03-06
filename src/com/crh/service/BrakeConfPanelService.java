package com.crh.service;

import java.util.ArrayList;
import java.util.List;

import com.crh2.dao.SQLHelper;
import com.crh2.javabean.TrainBrakeFactor;
import com.crh2.javabean.TrainElectricBrake;

/**
 * 为BrakeConfPanel提供数据增、删、改、查
 * @author huhui
 *
 */
public class BrakeConfPanelService {

    /**
     * 提供统一的数据库增、删、改、查操作
     */
    private static SQLHelper sqlHelper              = new SQLHelper();
    /**
     * 默认的制动系数
     */
    private static double    defaultBrakeFactor[][] = {
            { 0.0, 0.078336, 0.178336, 0.258336, 0.338336, 0.470668, 0.603001, 0.735334, 0.867667, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 2.0, 0.078464, 0.178464, 0.258464, 0.338464, 0.470771, 0.603078, 0.735386, 0.867693, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 4.0, 0.0786, 0.1786, 0.2586, 0.3386, 0.47088, 0.60316, 0.73544, 0.86772, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 6.0, 0.078743, 0.178743, 0.258743, 0.338743, 0.470994, 0.603246, 0.735497, 0.867749, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 8.0, 0.078893, 0.178893, 0.258893, 0.338893, 0.471115, 0.603336, 0.735557, 0.867779, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 10.0, 0.079051, 0.179051, 0.259051, 0.339051, 0.471241, 0.603431, 0.73562, 0.86781, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 12.0, 0.079216, 0.179216, 0.259216, 0.339216, 0.471373, 0.60353, 0.735686, 0.867843, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 14.0, 0.079389, 0.179389, 0.259389, 0.339389, 0.471511, 0.603633, 0.735755, 0.867878, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 16.0, 0.079568, 0.179568, 0.259568, 0.339568, 0.471655, 0.603741, 0.735827, 0.867914, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 18.0, 0.079756, 0.179756, 0.259756, 0.339756, 0.471804, 0.603853, 0.735902, 0.867951, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 20.0, 0.07995, 0.17995, 0.25995, 0.33995, 0.47196, 0.60397, 0.73598, 0.86799, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 22.0, 0.080152, 0.180152, 0.260152, 0.340152, 0.472122, 0.604091, 0.736061, 0.86803, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 24.0, 0.080361, 0.180361, 0.260361, 0.340361, 0.472289, 0.604217, 0.736145, 0.868072, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 26.0, 0.080578, 0.180578, 0.260578, 0.340578, 0.472462, 0.604347, 0.736231, 0.868116, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 28.0, 0.080802, 0.180802, 0.260802, 0.340802, 0.472641, 0.604481, 0.736321, 0.86816, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 30.0, 0.081033, 0.181033, 0.261033, 0.341033, 0.472826, 0.60462, 0.736413, 0.868207, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 32.0, 0.081272, 0.181272, 0.261272, 0.341272, 0.473017, 0.604763, 0.736509, 0.868254, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 34.0, 0.081518, 0.181518, 0.261518, 0.341518, 0.473214, 0.604911, 0.736607, 0.868303, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 36.0, 0.081771, 0.181771, 0.261771, 0.341771, 0.473417, 0.605062, 0.736708, 0.868354, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 38.0, 0.082031, 0.182032, 0.262031, 0.342032, 0.473625, 0.605219, 0.736813, 0.868406, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 40.0, 0.082299, 0.1823, 0.262299, 0.3423, 0.47384, 0.60538, 0.73692, 0.86846, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 42.0, 0.082575, 0.182575, 0.262575, 0.342575, 0.47406, 0.605545, 0.73703, 0.868515, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 44.0, 0.082858, 0.182858, 0.262858, 0.342858, 0.474286, 0.605715, 0.737143, 0.868572, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 46.0, 0.083148, 0.183148, 0.263148, 0.343148, 0.474518, 0.605889, 0.737259, 0.86863, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 48.0, 0.083445, 0.183445, 0.263445, 0.343445, 0.474756, 0.606067, 0.737378, 0.868689, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 50.0, 0.08375, 0.18375, 0.26375, 0.34375, 0.475, 0.60625, 0.7375, 0.86875, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 52.0, 0.084062, 0.184062, 0.264062, 0.344062, 0.475249, 0.606437, 0.737625, 0.868812, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 54.0, 0.084381, 0.184381, 0.264381, 0.344381, 0.475505, 0.606629, 0.737753, 0.868876, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 56.0, 0.084708, 0.184708, 0.264708, 0.344708, 0.475766, 0.606825, 0.737883, 0.868942, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 58.0, 0.085042, 0.185042, 0.265042, 0.345042, 0.476034, 0.607025, 0.738017, 0.869008, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 60.0, 0.085384, 0.185384, 0.265384, 0.345384, 0.476307, 0.60723, 0.738153, 0.869077, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 62.0, 0.085732, 0.185732, 0.265732, 0.345732, 0.476586, 0.607439, 0.738293, 0.869146, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 64.0, 0.086089, 0.186089, 0.266089, 0.346089, 0.476871, 0.607653, 0.738435, 0.869218, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 66.0, 0.086452, 0.186452, 0.266452, 0.346452, 0.477162, 0.607871, 0.738581, 0.86929, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 68.0, 0.086823, 0.186823, 0.266823, 0.346823, 0.477458, 0.608094, 0.738729, 0.869365, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 70.0, 0.087201, 0.187201, 0.267201, 0.347201, 0.477761, 0.608321, 0.73888, 0.86944, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 72.0, 0.087587, 0.187587, 0.267587, 0.347587, 0.478069, 0.608552, 0.739035, 0.869517, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 74.0, 0.08798, 0.18798, 0.26798, 0.34798, 0.478384, 0.608788, 0.739192, 0.869596, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 76.0, 0.08838, 0.18838, 0.26838, 0.34838, 0.478704, 0.609028, 0.739352, 0.869676, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 78.0, 0.088788, 0.188788, 0.268788, 0.348788, 0.47903, 0.609273, 0.739515, 0.869758, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 80.0, 0.089202, 0.189203, 0.269202, 0.349203, 0.479362, 0.609522, 0.739681, 0.869841, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 82.0, 0.089625, 0.189625, 0.269625, 0.349625, 0.4797, 0.609775, 0.73985, 0.869925, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 84.0, 0.090054, 0.190054, 0.270054, 0.350054, 0.480044, 0.610033, 0.740022, 0.870011, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 86.0, 0.090491, 0.190491, 0.270491, 0.350491, 0.480393, 0.610295, 0.740197, 0.870098, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 88.0, 0.090936, 0.190936, 0.270936, 0.350936, 0.480749, 0.610561, 0.740374, 0.870187, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 90.0, 0.091387, 0.191387, 0.271387, 0.351387, 0.48111, 0.610832, 0.740555, 0.870277, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 92.0, 0.091847, 0.191847, 0.271847, 0.351847, 0.481477, 0.611108, 0.740739, 0.870369, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 94.0, 0.092313, 0.192313, 0.272313, 0.352313, 0.48185, 0.611388, 0.740925, 0.870463, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 96.0, 0.092787, 0.192787, 0.272787, 0.352787, 0.482229, 0.611672, 0.741115, 0.870557, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 98.0, 0.093268, 0.193268, 0.273268, 0.353268, 0.482614, 0.611961, 0.741307, 0.870654, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 100.0, 0.093756, 0.193756, 0.273756, 0.353756, 0.483005, 0.612254, 0.741502, 0.870751, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 102.0, 0.094252, 0.194252, 0.274252, 0.354252, 0.483402, 0.612551, 0.741701, 0.87085, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 104.0, 0.094755, 0.194755, 0.274755, 0.354755, 0.483804, 0.612853, 0.741902, 0.870951, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 106.0, 0.095265, 0.195265, 0.275265, 0.355266, 0.484212, 0.613159, 0.742106, 0.871053, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 108.0, 0.095783, 0.195783, 0.275783, 0.355783, 0.484627, 0.61347, 0.742313, 0.871157, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 110.0, 0.096308, 0.196308, 0.276308, 0.356308, 0.485047, 0.613785, 0.742523, 0.871262, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 112.0, 0.096841, 0.196841, 0.276841, 0.356841, 0.485473, 0.614105, 0.742736, 0.871368, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 114.0, 0.097381, 0.197381, 0.277381, 0.357381, 0.485905, 0.614429, 0.742952, 0.871476, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 116.0, 0.097928, 0.197928, 0.277928, 0.357928, 0.486342, 0.614757, 0.743171, 0.871586, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 118.0, 0.098483, 0.198483, 0.278483, 0.358483, 0.486786, 0.61509, 0.743393, 0.871697, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 120.0, 0.099045, 0.199045, 0.279045, 0.359045, 0.487236, 0.615427, 0.743618, 0.871809, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 122.0, 0.099614, 0.199614, 0.279614, 0.359614, 0.487691, 0.615768, 0.743846, 0.871923, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 124.0, 0.10019, 0.20019, 0.28019, 0.36019, 0.488152, 0.616114, 0.744076, 0.872038, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 126.0, 0.100774, 0.200774, 0.280774, 0.360774, 0.488619, 0.616465, 0.74431, 0.872155, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 128.0, 0.101366, 0.201366, 0.281366, 0.361366, 0.489092, 0.616819, 0.744546, 0.872273, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 130.0, 0.101964, 0.201964, 0.281964, 0.361964, 0.489571, 0.617179, 0.744786, 0.872393, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 132.0, 0.10257, 0.20257, 0.28257, 0.36257, 0.490056, 0.617542, 0.745028, 0.872514, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 134.0, 0.103184, 0.203184, 0.283184, 0.363184, 0.490547, 0.61791, 0.745273, 0.872637, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 136.0, 0.103804, 0.203804, 0.283804, 0.363804, 0.491043, 0.618283, 0.745522, 0.872761, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 138.0, 0.104432, 0.204432, 0.284432, 0.364432, 0.491546, 0.618659, 0.745773, 0.872886, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 140.0, 0.105068, 0.205068, 0.285068, 0.365068, 0.492054, 0.619041, 0.746027, 0.873014, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 142.0, 0.10571, 0.20571, 0.28571, 0.36571, 0.492568, 0.619426, 0.746284, 0.873142, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 144.0, 0.10636, 0.20636, 0.28636, 0.36636, 0.493088, 0.619816, 0.746544, 0.873272, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 146.0, 0.107018, 0.207018, 0.287018, 0.367018, 0.493614, 0.620211, 0.746807, 0.873404, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 148.0, 0.107683, 0.207683, 0.287683, 0.367683, 0.494146, 0.62061, 0.747073, 0.873536, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 150.0, 0.108355, 0.208355, 0.288355, 0.368355, 0.494684, 0.621013, 0.747342, 0.873671, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 152.0, 0.109034, 0.209034, 0.289034, 0.369034, 0.495227, 0.621421, 0.747614, 0.873807, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 154.0, 0.109721, 0.209721, 0.289721, 0.369721, 0.495777, 0.621833, 0.747888, 0.873944, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 156.0, 0.110415, 0.210415, 0.290415, 0.370415, 0.496332, 0.622249, 0.748166, 0.874083, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 158.0, 0.111117, 0.211117, 0.291117, 0.371117, 0.496893, 0.62267, 0.748447, 0.874223, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 160.0, 0.111826, 0.211826, 0.291826, 0.371826, 0.49746, 0.623095, 0.74873, 0.874365, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 162.0, 0.112542, 0.212542, 0.292542, 0.372542, 0.493233, 0.613925, 0.734617, 0.855308, 0.976, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 164.0, 0.113265, 0.213265, 0.293265, 0.373265, 0.489012, 0.604759, 0.720506, 0.836253, 0.952, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 166.0, 0.113996, 0.213996, 0.293996, 0.373996, 0.484797, 0.595598, 0.706398, 0.817199, 0.928, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 168.0, 0.114734, 0.214734, 0.294734, 0.374734, 0.480588, 0.586441, 0.692294, 0.798147, 0.904, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 170.0, 0.11548, 0.21548, 0.29548, 0.37548, 0.476384, 0.577288, 0.678192, 0.779096, 0.88, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 172.0, 0.116233, 0.216233, 0.296233, 0.376233, 0.472186, 0.56814, 0.664093, 0.760047, 0.856, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 174.0, 0.116993, 0.216993, 0.296993, 0.376993, 0.467995, 0.558996, 0.649997, 0.740999, 0.832, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 176.0, 0.117761, 0.217761, 0.297761, 0.377761, 0.463809, 0.549856, 0.635904, 0.721952, 0.808, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 178.0, 0.118536, 0.218536, 0.298536, 0.378536, 0.459629, 0.540721, 0.621814, 0.702907, 0.784, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 180.0, 0.119318, 0.219318, 0.299318, 0.379318, 0.455455, 0.531591, 0.607727, 0.683864, 0.76, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 182.0, 0.120108, 0.220108, 0.300108, 0.380108, 0.451286, 0.522465, 0.593643, 0.664822, 0.736, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 184.0, 0.120905, 0.220905, 0.300905, 0.380905, 0.447124, 0.513343, 0.579562, 0.645781, 0.712, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 186.0, 0.121709, 0.221709, 0.301709, 0.381709, 0.444727, 0.507746, 0.570764, 0.633782, 0.6968, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 188.0, 0.122521, 0.222521, 0.302521, 0.382521, 0.444097, 0.505673, 0.567248, 0.628824, 0.6904, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 190.0, 0.12334, 0.22334, 0.30334, 0.38334, 0.443472, 0.503604, 0.563736, 0.623868, 0.684, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 192.0, 0.124166, 0.224166, 0.304166, 0.384166, 0.442853, 0.50154, 0.560227, 0.618913, 0.6776, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 194.0, 0.125, 0.225, 0.305, 0.385, 0.44224, 0.49948, 0.55672, 0.61396, 0.6712, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 196.0, 0.125841, 0.225841, 0.305841, 0.385841, 0.441633, 0.497425, 0.553217, 0.609008, 0.6648, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 198.0, 0.12669, 0.22669, 0.30669, 0.38669, 0.441032, 0.495374, 0.549716, 0.604058, 0.6584, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 200.0, 0.127546, 0.227546, 0.307546, 0.387546, 0.440436, 0.493327, 0.546218, 0.599109, 0.652, 0.0, 0.0, 0.0, 0.0, 0.0, 1.119403 },
            { 202.0, 0.128409, 0.228409, 0.308409, 0.388409, 0.439847, 0.491285, 0.542723, 0.594162, 0.6456, 0.0, 0.0, 0.0, 0.0, 0.0, 0.979478 },
            { 204.0, 0.129279, 0.229279, 0.309279, 0.389279, 0.439263, 0.489248, 0.539232, 0.589216, 0.6392, 0.0, 0.0, 0.0, 0.0, 0.0, 0.839552 },
            { 206.0, 0.130157, 0.230157, 0.310157, 0.390157, 0.438686, 0.487214, 0.535743, 0.584271, 0.6328, 0.0, 0.0, 0.0, 0.0, 0.0, 0.839552 },
            { 208.0, 0.131042, 0.231042, 0.311042, 0.391042, 0.438114, 0.485185, 0.532257, 0.579328, 0.6264, 0.0, 0.0, 0.0, 0.0, 0.0, 0.839552 },
            { 210.0, 0.131935, 0.231935, 0.311935, 0.391935, 0.437548, 0.483161, 0.528774, 0.574387, 0.62, 0.0, 0.0, 0.0, 0.0, 0.0, 0.839552 },
            { 212.0, 0.132835, 0.232835, 0.312835, 0.392835, 0.438096, 0.483358, 0.52862, 0.573881, 0.619143, 0.0, 0.0, 0.0, 0.0, 0.0, 0.839552 },
            { 214.0, 0.133742, 0.233742, 0.313742, 0.393742, 0.438651, 0.483559, 0.528468, 0.573377, 0.618286, 0.0, 0.0, 0.0, 0.0, 0.0, 0.839552 },
            { 216.0, 0.134656, 0.234656, 0.314656, 0.394656, 0.439211, 0.483765, 0.52832, 0.572874, 0.617429, 0.0, 0.0, 0.0, 0.0, 0.0, 0.839552 },
            { 218.0, 0.135578, 0.235578, 0.315578, 0.395578, 0.439777, 0.483976, 0.528174, 0.572373, 0.616571, 0.0, 0.0, 0.0, 0.0, 0.0, 0.839552 },
            { 220.0, 0.136508, 0.236508, 0.316508, 0.396508, 0.440349, 0.48419, 0.528032, 0.571873, 0.615714, 0.0, 0.0, 0.0, 0.0, 0.0, 0.839552 },
            { 222.0, 0.137444, 0.237444, 0.317444, 0.397444, 0.440927, 0.484409, 0.527892, 0.571375, 0.614857, 0.0, 0.0, 0.0, 0.0, 0.0, 0.839552 },
            { 224.0, 0.138388, 0.238388, 0.318388, 0.398388, 0.441511, 0.484633, 0.527755, 0.570878, 0.614, 0.0, 0.0, 0.0, 0.0, 0.0, 0.839552 },
            { 226.0, 0.13934, 0.23934, 0.31934, 0.39934, 0.4421, 0.484861, 0.527622, 0.570382, 0.613143, 0.0, 0.0, 0.0, 0.0, 0.0, 0.839552 },
            { 228.0, 0.140298, 0.240298, 0.320298, 0.400298, 0.442696, 0.485093, 0.527491, 0.569888, 0.612286, 0.0, 0.0, 0.0, 0.0, 0.0, 0.839552 },
            { 230.0, 0.141264, 0.241264, 0.321264, 0.401264, 0.443297, 0.48533, 0.527363, 0.569396, 0.611429, 0.0, 0.0, 0.0, 0.0, 0.0, 0.839552 },
            { 232.0, 0.142238, 0.242238, 0.322238, 0.402238, 0.443904, 0.485571, 0.527238, 0.568905, 0.610571, 0.0, 0.0, 0.0, 0.0, 0.0, 0.839552 },
            { 234.0, 0.143218, 0.243218, 0.323218, 0.403218, 0.444518, 0.485817, 0.527116, 0.568415, 0.609714, 0.0, 0.0, 0.0, 0.0, 0.0, 0.839552 },
            { 236.0, 0.144206, 0.244206, 0.324206, 0.404206, 0.445137, 0.486067, 0.526997, 0.567927, 0.608857, 0.0, 0.0, 0.0, 0.0, 0.0, 0.839552 },
            { 238.0, 0.145202, 0.245202, 0.325202, 0.405202, 0.445761, 0.486321, 0.526881, 0.56744, 0.608, 0.0, 0.0, 0.0, 0.0, 0.0, 0.839552 },
            { 240.0, 0.146205, 0.246205, 0.326205, 0.406205, 0.446392, 0.48658, 0.526768, 0.566955, 0.607143, 0.0, 0.0, 0.0, 0.0, 0.0, 0.839552 },
            { 242.0, 0.147215, 0.247215, 0.327215, 0.407215, 0.447029, 0.486843, 0.526657, 0.566472, 0.606286, 0.0, 0.0, 0.0, 0.0, 0.0, 0.839552 },
            { 244.0, 0.148232, 0.248232, 0.328232, 0.408232, 0.447671, 0.487111, 0.52655, 0.565989, 0.605429, 0.0, 0.0, 0.0, 0.0, 0.0, 0.839552 },
            { 246.0, 0.149257, 0.249257, 0.329257, 0.409257, 0.44832, 0.487383, 0.526446, 0.565509, 0.604571, 0.0, 0.0, 0.0, 0.0, 0.0, 0.839552 },
            { 248.0, 0.150289, 0.250289, 0.330289, 0.410289, 0.448974, 0.487659, 0.526344, 0.565029, 0.603714, 0.0, 0.0, 0.0, 0.0, 0.0, 0.839552 },
            { 250.0, 0.151329, 0.251329, 0.331329, 0.411329, 0.449634, 0.48794, 0.526246, 0.564551, 0.602857, 0.0, 0.0, 0.0, 0.0, 0.0, 0.839552 },
            { 252.0, 0.152375, 0.252375, 0.332375, 0.412375, 0.4503, 0.488225, 0.52615, 0.564075, 0.602, 0.0, 0.0, 0.0, 0.0, 0.0, 0.839552 },
            { 254.0, 0.15343, 0.25343, 0.33343, 0.41343, 0.450972, 0.488515, 0.526058, 0.5636, 0.601143, 0.0, 0.0, 0.0, 0.0, 0.0, 0.839552 },
            { 256.0, 0.154491, 0.254491, 0.334491, 0.414491, 0.45165, 0.488809, 0.525968, 0.563127, 0.600286, 0.0, 0.0, 0.0, 0.0, 0.0, 0.839552 },
            { 258.0, 0.15556, 0.25556, 0.33556, 0.41556, 0.452334, 0.489107, 0.525881, 0.562655, 0.599429, 0.0, 0.0, 0.0, 0.0, 0.0, 0.839552 },
            { 260.0, 0.156636, 0.256636, 0.336636, 0.416636, 0.453023, 0.48941, 0.525797, 0.562184, 0.598571, 0.0, 0.0, 0.0, 0.0, 0.0, 0.839552 },
            { 262.0, 0.15772, 0.25772, 0.33772, 0.41772, 0.453719, 0.489718, 0.525717, 0.561715, 0.597714, 0.0, 0.0, 0.0, 0.0, 0.0, 0.839552 },
            { 264.0, 0.158811, 0.258811, 0.338811, 0.418811, 0.45442, 0.490029, 0.525639, 0.561248, 0.596857, 0.0, 0.0, 0.0, 0.0, 0.0, 0.839552 },
            { 266.0, 0.159909, 0.259909, 0.339909, 0.419909, 0.455127, 0.490345, 0.525564, 0.560782, 0.596, 0.0, 0.0, 0.0, 0.0, 0.0, 0.839552 },
            { 268.0, 0.161015, 0.261015, 0.341015, 0.421015, 0.45584, 0.490666, 0.525492, 0.560317, 0.595143, 0.0, 0.0, 0.0, 0.0, 0.0, 0.839552 },
            { 270.0, 0.162128, 0.262128, 0.342128, 0.422128, 0.456559, 0.490991, 0.525422, 0.559854, 0.594286, 0.0, 0.0, 0.0, 0.0, 0.0, 0.839552 },
            { 272.0, 0.163248, 0.263248, 0.343248, 0.423248, 0.457284, 0.49132, 0.525356, 0.559392, 0.593429, 0.0, 0.0, 0.0, 0.0, 0.0, 0.839552 },
            { 274.0, 0.164376, 0.264376, 0.344376, 0.424376, 0.458015, 0.491654, 0.525293, 0.558932, 0.592571, 0.0, 0.0, 0.0, 0.0, 0.0, 0.839552 },
            { 276.0, 0.165511, 0.265511, 0.345511, 0.425511, 0.458751, 0.491992, 0.525233, 0.558474, 0.591714, 0.0, 0.0, 0.0, 0.0, 0.0, 0.839552 },
            { 278.0, 0.166653, 0.266653, 0.346653, 0.426653, 0.459494, 0.492335, 0.525175, 0.558016, 0.590857, 0.0, 0.0, 0.0, 0.0, 0.0, 0.839552 },
            { 280.0, 0.167803, 0.267803, 0.347803, 0.427803, 0.460242, 0.492682, 0.525121, 0.557561, 0.59, 0.0, 0.0, 0.0, 0.0, 0.0, 0.839552 },
            { 282.0, 0.16896, 0.26896, 0.34896, 0.42896, 0.460996, 0.493033, 0.52507, 0.557106, 0.589143, 0.0, 0.0, 0.0, 0.0, 0.0, 0.839552 },
            { 284.0, 0.170124, 0.270124, 0.350124, 0.430124, 0.461756, 0.493389, 0.525021, 0.556653, 0.588286, 0.0, 0.0, 0.0, 0.0, 0.0, 0.839552 },
            { 286.0, 0.171296, 0.271296, 0.351296, 0.431296, 0.462522, 0.493749, 0.524975, 0.556202, 0.587429, 0.0, 0.0, 0.0, 0.0, 0.0, 0.839552 },
            { 288.0, 0.172475, 0.272475, 0.352475, 0.432475, 0.463294, 0.494114, 0.524933, 0.555752, 0.586571, 0.0, 0.0, 0.0, 0.0, 0.0, 0.839552 },
            { 290.0, 0.173661, 0.273661, 0.353661, 0.433661, 0.464072, 0.494483, 0.524893, 0.555304, 0.585714, 0.0, 0.0, 0.0, 0.0, 0.0, 0.839552 },
            { 292.0, 0.174855, 0.274855, 0.354855, 0.434855, 0.464856, 0.494856, 0.524856, 0.554857, 0.584857, 0.0, 0.0, 0.0, 0.0, 0.0, 0.839552 },
            { 294.0, 0.176056, 0.276056, 0.356056, 0.436056, 0.465645, 0.495234, 0.524823, 0.554411, 0.584, 0.0, 0.0, 0.0, 0.0, 0.0, 0.839552 },
            { 296.0, 0.177265, 0.277265, 0.357265, 0.437265, 0.46644, 0.495616, 0.524792, 0.553967, 0.583143, 0.0, 0.0, 0.0, 0.0, 0.0, 0.839552 },
            { 298.0, 0.178481, 0.278481, 0.358481, 0.438481, 0.467242, 0.496003, 0.524764, 0.553525, 0.582286, 0.0, 0.0, 0.0, 0.0, 0.0, 0.839552 },
            { 300.0, 0.179704, 0.279704, 0.359704, 0.439704, 0.468049, 0.496394, 0.524739, 0.553084, 0.581429, 0.0, 0.0, 0.0, 0.0, 0.0, 0.839552 } };

    /**
     * 根据trainCategoryId从train_info获得三个参数
     * @param trainCategoryId
     * @return
     */
    public static double[] getThreeParameters(int trainCategoryId) {
        double[] parameters = new double[3];
        String sql = "SELECT maxV,M,ar FROM train_info WHERE train_category_id = " + trainCategoryId;
        List list = sqlHelper.query(sql, null);
        if (list.size() == 0) {
            for (int i = 0; i < parameters.length; i++) {
                parameters[i] = 0;
            }
        } else {
            parameters[0] = Double.parseDouble(((Object[]) list.get(0))[0].toString());
            parameters[1] = Double.parseDouble(((Object[]) list.get(0))[1].toString());
            parameters[2] = Double.parseDouble(((Object[]) list.get(0))[2].toString());
        }
        return parameters;
    }

    /**
     * 根据trainCategoryId从train_traction_conf获得6个参数
     * @param trainCategoryId
     * @return
     */
    public static String[] getFiveParameters(int trainCategoryId) {
        String[] parameters = new String[6];
        String sql = "SELECT k1,k2,D,N,T,F2 FROM train_traction_conf WHERE train_category_id = " + trainCategoryId;
        List list = sqlHelper.query(sql, null);
        if (list.size() == 0) {
            for (int i = 0; i < parameters.length; i++) {
                parameters[i] = 0 + "";
            }
        } else {
            parameters[0] = ((Object[]) list.get(0))[0].toString();
            parameters[1] = ((Object[]) list.get(0))[1].toString();
            parameters[2] = ((Object[]) list.get(0))[2].toString();
            parameters[3] = ((Object[]) list.get(0))[3].toString();
            parameters[4] = ((Object[]) list.get(0))[4].toString();
            parameters[5] = ((Object[]) list.get(0))[5].toString();
        }
        return parameters;
    }

    /**
     * 根据trainCategoryId从train_traction_conf获得6个参数
     * @param trainCategoryId
     * @return
     */
    public static String[] getFiveParameters(int id, int trainCategoryId) {
        String[] parameters = new String[6];
        String sql = "SELECT k1,k2,D,N,T,F2 FROM train_traction_conf WHERE train_category_id = " + trainCategoryId;
        if (id != -1) {
            sql += " and id = " + id;
        }
        List list = sqlHelper.query(sql, null);
        if (list.size() == 0) {
            for (int i = 0; i < parameters.length; i++) {
                parameters[i] = 0 + "";
            }
        } else {
            parameters[0] = ((Object[]) list.get(0))[0].toString();
            parameters[1] = ((Object[]) list.get(0))[1].toString();
            parameters[2] = ((Object[]) list.get(0))[2].toString();
            parameters[3] = ((Object[]) list.get(0))[3].toString();
            parameters[4] = ((Object[]) list.get(0))[4].toString();
            parameters[5] = ((Object[]) list.get(0))[5].toString();
        }
        return parameters;
    }

    /**
     * 查询train_electric_brake
     * @param trainCategoryId
     * @return
     */
    public static TrainElectricBrake getTrainElectricBrake(int trainCategoryId) {
        TrainElectricBrake trainElectricBrake = new TrainElectricBrake();
        String sql = "SELECT * FROM  train_electric_brake WHERE train_category_id = " + trainCategoryId;
        List list = sqlHelper.query(sql, null);
        if (list.size() != 0) {
            Object[] obj = (Object[]) list.get(0);
            trainElectricBrake.setId(Integer.parseInt(obj[0].toString()));
            trainElectricBrake.setV1(Double.parseDouble(obj[1].toString()));
            trainElectricBrake.setV2(Double.parseDouble(obj[2].toString()));
            trainElectricBrake.setP00(Double.parseDouble(obj[3].toString()));
            trainElectricBrake.setTrainCategoryId(Integer.parseInt(obj[4].toString()));
        }

        return trainElectricBrake;
    }

    /**
     * 保存train_electric_brake
     * @param trainCategoryId
     * @param trainElectricBrake
     */
    public static void saveTrainElectricBrake(int trainCategoryId, TrainElectricBrake trainElectricBrake) {
        String delSQL = "delete from train_electric_brake where train_category_id = " + trainCategoryId;
        sqlHelper.update(delSQL, null);
        String sql = "insert into train_electric_brake values(null," + trainElectricBrake.getV1() + "," + trainElectricBrake.getV2() + ","
                     + trainElectricBrake.getP00() + "," + trainElectricBrake.getTrainCategoryId() + ")";
        sqlHelper.update(sql, null);
    }

    /**
     * 根据trainCategoryId查询train_brake_factor
     * @param trainCategoryId
     * @return
     */
    public static ArrayList<TrainBrakeFactor> getBrakeFactor(int trainCategoryId) {
        ArrayList<TrainBrakeFactor> factorList = new ArrayList<TrainBrakeFactor>();
        String sql = "SELECT * FROM train_brake_factor WHERE train_category_id = " + trainCategoryId;
        List list = sqlHelper.query(sql, null);
        if (list.size() != 0) {
            for (int i = 0; i < list.size(); i++) {
                Object[] obj = (Object[]) list.get(i);
                TrainBrakeFactor factor = new TrainBrakeFactor();
                factor.setId(Integer.parseInt(obj[0].toString()));
                factor.setSpeed(Double.parseDouble(obj[1].toString()));
                factor.set_1A(Double.parseDouble(obj[2].toString()));
                factor.set_1B(Double.parseDouble(obj[3].toString()));
                factor.set_2(Double.parseDouble(obj[4].toString()));
                factor.set_3(Double.parseDouble(obj[5].toString()));
                factor.set_4(Double.parseDouble(obj[6].toString()));
                factor.set_5(Double.parseDouble(obj[7].toString()));
                factor.set_6(Double.parseDouble(obj[8].toString()));
                factor.set_7(Double.parseDouble(obj[9].toString()));
                factor.set_8(Double.parseDouble(obj[10].toString()));
                factor.set_9(Double.parseDouble(obj[11].toString()));
                factor.set_10(Double.parseDouble(obj[12].toString()));
                factor.set_11(Double.parseDouble(obj[13].toString()));
                factor.set_12(Double.parseDouble(obj[14].toString()));
                factor.set_13(Double.parseDouble(obj[15].toString()));
                factor.setEb(Double.parseDouble(obj[16].toString()));
                factor.setTrainCategoryId(Integer.parseInt(obj[17].toString()));
                factorList.add(factor);
            }
        } else {
            for (int i = 0; i < defaultBrakeFactor.length; i++) {
                TrainBrakeFactor factor = new TrainBrakeFactor();
                factor.setSpeed(defaultBrakeFactor[i][0]);
                factor.set_1A(defaultBrakeFactor[i][1]);
                factor.set_1B(defaultBrakeFactor[i][2]);
                factor.set_2(defaultBrakeFactor[i][3]);
                factor.set_3(defaultBrakeFactor[i][4]);
                factor.set_4(defaultBrakeFactor[i][5]);
                factor.set_5(defaultBrakeFactor[i][6]);
                factor.set_6(defaultBrakeFactor[i][7]);
                factor.set_7(defaultBrakeFactor[i][8]);
                factor.set_8(defaultBrakeFactor[i][9]);
                factor.set_9(defaultBrakeFactor[i][10]);
                factor.set_10(defaultBrakeFactor[i][11]);
                factor.set_11(defaultBrakeFactor[i][12]);
                factor.set_12(defaultBrakeFactor[i][13]);
                factor.set_13(defaultBrakeFactor[i][14]);
                factor.setEb(defaultBrakeFactor[i][15]);
                factorList.add(factor);
            }
        }

        return factorList;
    }

    /**
     * 保存train_brake_factor
     * @param trainCategoryId
     * @param factorList
     */
    public static void saveBrakeFactor(int trainCategoryId, ArrayList<TrainBrakeFactor> factorList) {
        if (factorList == null || factorList.size() == 0) {
            return;
        }
        String delSQL = "delete from train_brake_factor where train_category_id = " + trainCategoryId;
        sqlHelper.update(delSQL, null);
        //批量插入
        ArrayList<String> sqlList = new ArrayList<String>();
        for (TrainBrakeFactor factor : factorList) {
            String insertSQL = "insert into train_brake_factor values(null," + factor.getSpeed() + "," + factor.get_1A() + "," + factor.get_1B() + ","
                               + factor.get_2() + "," + factor.get_3() + "," + factor.get_4() + "," + factor.get_5() + "," + factor.get_6() + ","
                               + factor.get_7() + "," + factor.get_8() + "," + factor.get_9() + "," + factor.get_10() + "," + factor.get_11() + ","
                               + factor.get_12() + "," + factor.get_13() + "," + factor.getEb() + "," + +factor.getTrainCategoryId() + ")";
            sqlList.add(insertSQL);
        }
        sqlHelper.batchInsert(sqlList);
    }

    /**
     * 判断显示哪些制动级位
     */
    public static boolean[] getBrakeLevelBoolean(ArrayList<TrainBrakeFactor> brakeFactorList) {
        int length = 15;
        boolean[] brakeLevelBoolean = new boolean[length];
        double[] sum = new double[length];
        for (TrainBrakeFactor bean : brakeFactorList) {
            sum[0] += bean.get_1A();
            sum[1] += bean.get_1B();
            sum[2] += bean.get_2();
            sum[3] += bean.get_3();
            sum[4] += bean.get_4();
            sum[5] += bean.get_5();
            sum[6] += bean.get_6();
            sum[7] += bean.get_7();
            sum[8] += bean.get_8();
            sum[9] += bean.get_9();
            sum[10] += bean.get_10();
            sum[11] += bean.get_11();
            sum[12] += bean.get_12();
            sum[13] += bean.get_13();
            sum[14] += bean.getEb();
        }
        for (int i = 0; i < length; i++) {
            if (sum[i] == 0) {
                brakeLevelBoolean[i] = false; //如果系数相加为0，则不显示该列
            } else {
                brakeLevelBoolean[i] = true;
            }
        }
        return brakeLevelBoolean;
    }

}
