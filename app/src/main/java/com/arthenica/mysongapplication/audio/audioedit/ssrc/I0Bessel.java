/*
 * Copyright(C) 1996 Takuya OOURA (email: ooura@mmm.t.u-tokyo.ac.jp).
 * You may use, copy, modify this code for any purpose and
 * without fee. You may distribute this ORIGINAL package.
 */
package com.arthenica.mysongapplication.audio.audioedit.ssrc;


/**
 * Bessel I_0().
 *
 * @author <a href="mailto:ooura@mmm.t.u-tokyo.ac.jp">Takuya OOURA</a>
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 060127 nsano port to java version <br>
 */
public class I0Bessel {
    /** */
    private static final double[] a = {
        8.5246820682016865877e-11, 2.5966600546497407288e-9,
        7.9689994568640180274e-8, 1.9906710409667748239e-6,
        4.0312469446528002532e-5, 6.4499871606224265421e-4,
        0.0079012345761930579108, 0.071111111109207045212,
        0.444444444444724909, 1.7777777777777532045,
        4.0000000000000011182, 3.99999999999999998,
        1.0000000000000000001,
        1.1520919130377195927e-10, 2.2287613013610985225e-9,
        8.1903951930694585113e-8, 1.9821560631611544984e-6,
        4.0335461940910133184e-5, 6.4495330974432203401e-4,
        0.0079013012611467520626, 0.071111038160875566622,
        0.44444450319062699316, 1.7777777439146450067,
        4.0000000132337935071, 3.9999999968569015366,
        1.0000000003426703174,
        1.5476870780515238488e-10, 1.2685004214732975355e-9,
        9.2776861851114223267e-8, 1.9063070109379044378e-6,
        4.0698004389917945832e-5, 6.4370447244298070713e-4,
        0.0079044749458444976958, 0.071105052411749363882,
        0.44445280640924755082, 1.7777694934432109713,
        4.0000055808824003386, 3.9999977081165740932,
        1.0000004333949319118,
        2.0675200625006793075e-10, -6.1689554705125681442e-10,
        1.2436765915401571654e-7, 1.5830429403520613423e-6,
        4.2947227560776583326e-5, 6.3249861665073441312e-4,
        0.0079454472840953930811, 0.070994327785661860575,
        0.44467219586283000332, 1.7774588182255374745,
        4.0003038986252717972, 3.9998233869142057195,
        1.0000472932961288324,
        2.7475684794982708655e-10, -3.8991472076521332023e-9,
        1.9730170483976049388e-7, 5.9651531561967674521e-7,
        5.1992971474748995357e-5, 5.7327338675433770752e-4,
        0.0082293143836530412024, 0.069990934858728039037,
        0.44726764292723985087, 1.7726685170014087784,
        4.0062907863712704432, 3.9952750700487845355,
        1.0016354346654179322
    };
    /** */
    private static final double[] b = {
        6.7852367144945531383e-8, 4.6266061382821826854e-7,
        6.9703135812354071774e-6, 7.6637663462953234134e-5,
        7.9113515222612691636e-4, 0.0073401204731103808981,
        0.060677114958668837046, 0.43994941411651569622,
        2.7420017097661750609, 14.289661921740860534,
        59.820609640320710779, 188.78998681199150629,
        399.8731367825601118, 427.56411572180478514,
        1.8042097874891098754e-7, 1.2277164312044637357e-6,
        1.8484393221474274861e-5, 2.0293995900091309208e-4,
        0.0020918539850246207459, 0.019375315654033949297,
        0.15985869016767185908, 1.1565260527420641724,
        7.1896341224206072113, 37.354773811947484532,
        155.80993164266268457, 489.5211371158540918,
        1030.9147225169564806, 1093.5883545113746958,
        4.8017305613187493564e-7, 3.261317843912380074e-6,
        4.9073137508166159639e-5, 5.3806506676487583755e-4,
        0.0055387918291051866561, 0.051223717488786549025,
        0.42190298621367914765, 3.0463625987357355872,
        18.895299447327733204, 97.915189029455461554,
        407.13940115493494659, 1274.3088990480582632,
        2670.9883037012547506, 2815.7166284662544712,
        1.2789926338424623394e-6, 8.6718263067604918916e-6,
        1.3041508821299929489e-4, 0.001428224737372747892,
        0.014684070635768789378, 0.13561403190404185755,
        1.1152592585977393953, 8.0387088559465389038,
        49.761318895895479206, 257.2684232313529138,
        1066.8543146269566231, 3328.3874581009636362,
        6948.8586598121634874, 7288.4893398212481055,
        3.409350368197032893e-6, 2.3079025203103376076e-5,
        3.4691373283901830239e-4, 0.003794994977222908545,
        0.038974209677945602145, 0.3594948380414878371,
        2.9522878893539528226, 21.246564609514287056,
        131.28727387146173141, 677.38107093296675421,
        2802.3724744545046518, 8718.5731420798254081,
        18141.348781638832286, 18948.925349296308859
    };
    /** */
    private static final double[] c = {
        2.5568678676452702768e-15, 3.0393953792305924324e-14,
        6.3343751991094840009e-13, 1.5041298011833009649e-11,
        4.4569436918556541414e-10, 1.746393051427167951e-8,
        1.0059224011079852317e-6, 1.0729838945088577089e-4,
        0.05150322693642527738,
        5.2527963991711562216e-15, 7.202118481421005641e-15,
        7.2561421229904797156e-13, 1.482312146673104251e-11,
        4.4602670450376245434e-10, 1.7463600061788679671e-8,
        1.005922609132234756e-6, 1.0729838937545111487e-4,
        0.051503226936437300716,
        1.3365917359358069908e-14, -1.2932643065888544835e-13,
        1.7450199447905602915e-12, 1.0419051209056979788e-11,
        4.58047881980598326e-10, 1.7442405450073548966e-8,
        1.0059461453281292278e-6, 1.0729837434500161228e-4,
        0.051503226940658446941,
        5.3771611477352308649e-14, -1.1396193006413731702e-12,
        1.2858641335221653409e-11, -5.9802086004570057703e-11,
        7.3666894305929510222e-10, 1.6731837150730356448e-8,
        1.0070831435812128922e-6, 1.0729733111203704813e-4,
        0.051503227360726294675,
        3.7819492084858931093e-14, -4.8600496888588034879e-13,
        1.6898350504817224909e-12, 4.5884624327524255865e-11,
        1.2521615963377513729e-10, 1.8959658437754727957e-8,
        1.0020716710561353622e-6, 1.073037119856927559e-4,
        0.05150322383300230775
    };

    /**
     *
     * @param x
     * @return
     */
    public static double value(double x) {
        int k;
        double w, t, y;
        w = Math.abs(x);
        if (w < 8.5) {
            t = w * w * 0.0625;
            k = 13 * ((int) t);
            y = (((((((((((a[k] * t + a[k + 1]) * t +
                a[k + 2]) * t + a[k + 3]) * t + a[k + 4]) * t +
                a[k + 5]) * t + a[k + 6]) * t + a[k + 7]) * t +
                a[k + 8]) * t + a[k + 9]) * t + a[k + 10]) * t +
                a[k + 11]) * t + a[k + 12];
        } else if (w < 12.5) {
            k = (int) w;
            t = w - k;
            k = 14 * (k - 8);
            y = ((((((((((((b[k] * t + b[k + 1]) * t +
                b[k + 2]) * t + b[k + 3]) * t + b[k + 4]) * t +
                b[k + 5]) * t + b[k + 6]) * t + b[k + 7]) * t +
                b[k + 8]) * t + b[k + 9]) * t + b[k + 10]) * t +
                b[k + 11]) * t + b[k + 12]) * t + b[k + 13];
        } else {
            t = 60 / w;
            k = 9 * ((int) t);
            y = ((((((((c[k] * t + c[k + 1]) * t +
                c[k + 2]) * t + c[k + 3]) * t + c[k + 4]) * t +
                c[k + 5]) * t + c[k + 6]) * t + c[k + 7]) * t +
                c[k + 8]) * Math.sqrt(t) * Math.exp(w);
        }
        return y;
    }
}

/* */
