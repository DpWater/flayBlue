package Test;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2020/2/24.
 */
public class FreemarkeExportrWordUtil {
    /** 默FreeMarker配置实例 */
    private static final Configuration configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);

    /** 默认采用UTF-8编码 */
    private static final String ENCODING = "UTF-8";

    /** buffer */
    private static final int BUFFER_SIZE = 1024;
     static final String img = "/9j/4AAQSkZJRgABAQEASABIAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0a\n" +
            "HBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIy\n" +
            "MjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAARCAF5AfQDASIA\n" +
            "AhEBAxEB/8QAHAAAAAcBAQAAAAAAAAAAAAAAAAECAwQFBgcI/8QARhAAAQMDAgQEBAQEBQMEAQIH\n" +
            "AQIDEQAEIRIxBUFRYRMicYEGMpGhFLHB0SNC4fAHFTNS8TRichYkQ4JzRJIXNTZTg6LC/8QAFgEB\n" +
            "AQEAAAAAAAAAAAAAAAAAAAEC/8QAHhEBAQEAAgMBAQEAAAAAAAAAAAERITECEkFRYXH/2gAMAwEA\n" +
            "AhEDEQA/AOGHccqSDJwBvTriZSk7EijSypUFOxxQJCiMRSwU+lJCFaZA8005cNBtKVpVIUOXI0CO\n" +
            "XWkmadabDgGo+YwBFNwdcT6UAC4VE+1OEEjAHvTegkTmBuKeDeltKiNzgGgIQNxRpAM/lTZkqUOX\n" +
            "IRQSopSARJ3kUD8zRQBJj70wtZ1gDPORTgJiYgmqDORnblQSmTJB+tEsEkkbdqSpUJ8p7ZoFFAk7\n" +
            "jvRlME0bYSopmRJ3mnFoCWhvq1H0ioGMTg7dKMETO1JEqVEnPOj+REAYnBoAcmQPekgAETmjSSTv\n" +
            "vRqJ35CgGRuKImcRQChEBU0FHzbg42mgIxknFFBPejnVRxJxQJHzROKJUxG3tSymDyoyD7xyoGTg\n" +
            "DM5zR4IH0pUbjvStEHIzQN4gAUWgqEGnPCMhWQeRoihW0+tAkJAnFDTt+9KCTmedDTO9AgpR3NFp\n" +
            "FL9ADQgkT+lAiIOIFJ0yMwTTmmRn8qMJ7R7UCAkAg70c8oTAFHE4ifakFJmgB+lKSNJkESKARIzQ\n" +
            "UmO1APKQfLvSeYxIoEb0XpvQA+maIADl96EkYo6AYjpQCRmjyPWiBzjagBGO9I3H9KcG2d6SVAzi\n" +
            "gR+dHpj1oyARQPfFARyP6UgAjlSz3NDE8qBM5xQ2oyRzooHWgGSKMHH7UczNETHrVCknrtTg3n7U\n" +
            "xjrTkwBneoHZjoIpYUY5xTKT3pxJjvQBeSZxSRjn2pWDzoRG0xQHA6j3oUQA5k0KBxpOpSQepFPp\n" +
            "SG8pUDB/s0S2iytLkEQYUD/KoU+GgpSgrylXId9j6UAaZC320nCVrlKuk0/dcNW34jSx/ISD3GRH\n" +
            "sak2lqHrV1RCgGhKjyHfFXKrNS2VBxP8ZoIUlU4UDuPvVRmbewDlo062uHBnO3pUbiNuba4gD5fK\n" +
            "es1t7PgX4Ru3dukaISChJmVEjb8zUNzhTHEGVIeCmn9ClagNQ1Jzt3orKBgnyokyIHXIkVMFsVWh\n" +
            "KkkFtWQeX9zVrwyytmrhpbpWpOpIdTGnTncelWt7btKbcShSS2oglUQFyNx78qgw7qNLyxEAHEYp\n" +
            "pSSkExOYq44lYlohSST5YV6ioVxbw2tA3PmjnjcVRXkEISuJIMU6iTjM0FIUhpQGQBqg79KVbjCV\n" +
            "Eb5GeYqAE8iDNEpKpRAwTBNOOCHcJOZEUtCCkAkYKsculAlSQG0KG46UcFx1tIOSTPanVJwpAA+a\n" +
            "fvFNMyl3VBOkEYoGQgDSSZgnFB4hTbSM4nH3qTbWztypDSEHYqJ96S9bqU9pnUv/AGpzp9aCKhOt\n" +
            "YE6QTknkKe8NKtQSYHKd4708hKWojSs9jMf1o3Ew2iBKQZVB3oIhZWIKYg9RvSS2SAZkdKnS44VD\n" +
            "SlJMSvYJHQcgKS40hsD+KlR/7aCAkET9YpYUQMDM7VN/EaWvDCGikc1Ngn2NJU6hSMtpEDdOJoIy\n" +
            "tXPA50QOrI/OgD4kzIz0pWnaMjbFASJSJwDTgSok8qASAO/KnRgZ51QlKITkUWmeUgbU7jHM96SZ\n" +
            "IkbelAx8u8UkRnnTriZjHKgE+Wd4oGVp0mQfajGTHOnQkc8kbdqSUwZ5zUCBAjn2o4IzJ9KcKJFJ\n" +
            "jITG9AiJxNGoCO/alKAnAiKJQ7+ooERjJoo6iPvSogwck86WUYnpQMHqYj0pKoB/andgJApKo5RQ\n" +
            "IH6c6PBEdaKBAM0eIwKBMajFGRtGPWjUkBWKBye1AkpkT+VJ0iedKIM7mgR3E0CSCdhRFMc6WNv3\n" +
            "o4nlGKBvEZpJGN6WUnqPekkEb1UII96KlkCeXrSYg7iagMGRRRPalc+W3SkqzO1FA9jRz3NJ5YoT\n" +
            "/SgdCoG9LCh1jtTAOd6WkkZFA8N6HKN4pEmJmjB74oFyeVCkTnehQbW+sWrlpQAQXNUFcGCOX98q\n" +
            "hL4NdM28vNlTaVQFFXyj16far1lDq3VW7yfCcCdcDG3rzH5VbjhLjrYatVp8Ug6rdwQD+/tQjN2N\n" +
            "oq1BSpzTkEBX84/UVc2lm25cKfUlRSkJKQD8/UD6iiuLVxS0NpcToaAJncBXLpPKry08Fll0uFKE\n" +
            "pRGkjzR29/yon0XEWVW7gTqJKWlRABAwAPcZqR/lKFrQBIcKlZI6pzIO+JqFdrDlwwtDitRQITOB\n" +
            "6/mas2HUW7WlaHXHQQsEfMR2HWiqFjg7bV7eoShRCToC5hIB2JBzOwB50QskJtvAQptDbCQSogqn\n" +
            "nJNXiy0Lh1LbDivDSEgfMqDnO0+nKoDN68m4CE25aaEpAjSVZO8nNVbER7hFvdNSFpWQAVaU+2oT\n" +
            "y2+tZa+4G6lxbZbOprmOfcfn9a3Vkl5u4ATlE6Y1gwCZIHbtSb9pCUuulwSFbpT8wO+PUVNM/HMn\n" +
            "rANXLjalakrADat5GYP1FVtv/qBKzGkmB0roXEOD6mUrUCvQnBSIwTiO4NYZq1cZ4qUqCgpCla55\n" +
            "QDmqhCUhbrJVIBwqrE8MgusnBCQtE4nMftSVWRVcsqQhQBGcTncfWK1Ni23cWaPD8MuHzFCtyQPs\n" +
            "MH60RkW2B+HSsphK3PD1Rt/c0gWTqXAzEvPGAPtP99K3F3wMXNu7bsqSlTpDiJIwsCRn7Gotpwdd\n" +
            "yQ4nSHBpbIJklOI9Mn7UVXP2qbPhxYsyQtYDb7xgSJ5dBM+tVTdip57wWCAzGowqZA/3KjPtWjvu\n" +
            "FPPrds0qCWEKAU4uBATj9B9anscNaS5b2aSlpDSdTiRJGhImT3kmaiMkizSCVKIGYIA2EZ9MfT1q\n" +
            "ufWdSlpGlKsoSPpV9deZboQhKEHVCSfmSMme5xWdfknSJJmBjeimVLIwVTGwAxRglcGUx6UtNs5u\n" +
            "pMiPmpDgSkqMyTjSM/egUlLYkKVpjbnP0pCtEfN9qCEJA1KCldUpx96MvBAhLaJ7gn86BoJ824mn\n" +
            "EIIjAnqDUlF6yUaXbO1UOZCSg/UGmyWlKHhgpB5TIoC0zyJo0RrUB70spg5AzzBosRq+9UGUgAEn\n" +
            "0AoiZ2IozBGqcetNpgSBQGgFSSo/SlFPkMfcUSPKCDE0a1ShQ7UDaYhXMRRlAOw5UG0hLeQM96dg\n" +
            "BW9Ay0gpG2QOdAgDKj706uMEmorqiSPsKgJR3G8c6IKg9/Sk5zJobmIzQLCuU47UsE5imQc9M0uS\n" +
            "floCIzGKSoZ3waQVyDJ25UaVdJ96AwBEYj0oAZmlp33MdYoKB2BEdqBMSDtO00nSBTgTAzFEcCOV\n" +
            "A2RyP3ojnJ/KjVM7iiJzFAlQJwIoxk74oSSNhRgGdqAGOsCkxA3pyI5e9Jk7ETQIKe0Ukpz29KdI\n" +
            "MYFIIoGzEgUmMmlkfWkk70QRyOU0n6RRwaFFAUsEk0gbUcyaBeoilSQKbBjnmj1TMTQODsaFI1Dn\n" +
            "PsKFB262davWmFXTCC4gwHE4ORBmpUs6UNpIStolKVrOwjY+hqmsLgWqH0LQHEpVp1TtNB+/Qmwf\n" +
            "RAWtZBWsbiMClP6U0ltxxISB4SV+ZY5nt12pu6vA+6ArCvmUYxjAAnJNVL14paFNtw22kglKcxyp\n" +
            "tlbouIjKcpJI36n0/vehvC7tXSLkh50JcAgJz5R/U1eN3KUhtxKFeKElJcPlAT0xuaylu+BDyUFa\n" +
            "VHClGFK6n+m1TmL8tqQAhx10mEpC/Lnl/wARRF+74fgLAuCohGoIZRKdPckYqFc/h3LRtV0Ekxht\n" +
            "nylZ6kj+lB++vTZqQtbdvqkJBMiOkk5qCph1drrDjhejD+mR6dvepq7p9Li2XSltKENnGlEnlEQM\n" +
            "0q6K3XEeAFAwAUqTAgZJ7jNVb1hdN3Oi3P4h0DJBSYUee8Gl2y7nU2hxVwQgAAkaUp/STtVNWiLx\n" +
            "3xAkKkRlKhMgbD6RUPiXBWbp65uGWEkrRJIkAyYVB9x9KSypVsQogBTYBbz3P19auk3Ytrq3Inwl\n" +
            "EaoONRH5TNXCcqJz4e/AW7jqtZcBVpg4AMx9cR3Ap7hNiHmw/pSFp0lUYDqSfmHOZk+1ax+2TeMJ\n" +
            "acUoIcSkE8wQcDtnamRatWhWW06mHk+UndCp8w7ZE/8ANQwxxFoWNqlhkB0oVKVkSSn25jaq3hzD\n" +
            "zl2ttgaAttIj/csHmPvVndPqFwh2AUIgKQD8snMft3onBbs3BdYWouKOoJ3EQd6NYjPcOcatkFZw\n" +
            "uSUDczEE94mmVWS9VwEgfxGwkA76Rn7mrpx/TY3LijqUSNAB2wB+ZqKy8LnQ2gA/w9JI5JT/AFzV\n" +
            "Rh/8mcfVcOFcKkJAUI1jVB/WoKeEgSUpUt1SiAEI1T+wre/gUrKG0OAJ0ylauQTuT9ce1M3C/wDL\n" +
            "rdQt0hDKQUqWfmWdz5t57zUqMG/Zrt2iHbZw8pMD2gCfeqhdukOFSrdKAMhEFRrQ3PGypxQYlptc\n" +
            "QllBUfcxM1TXN04UHw1+pcBE/b86CNotyzreS4pfJAxn6frUJwpUohLaUQf939afdUtfnW0gp/7B\n" +
            "tUcFognWlJ5DNAYt1KThxvuCsD86L8O4CSCCBvBmkrQmJSZ9NqJBKVgjCh0oHEOFGFe1EtZEQqac\n" +
            "K0u/Npk9MTTRCRjpQKC9RzAzS8aSJxEU2SIxHagFRgz3mqDJ0ZnHpSm3QpCsRiYpKczH/FECZAHP\n" +
            "egeRhtIJyetGCSZn602FgoxuOtH4kASQKIbfWUgEETsJporKoB3pxSdaMkZ7U04nSInFRSTtg4FE\n" +
            "SSY2oioHmPegM0CxB9e1KBz2psAA7/SlAiIkUUFRJJj1602o5wIxypwgECjCCTIFEJQSCMk0vVkm\n" +
            "aLaJo1oMggSDQKjy4Ag02diN+dOhMpkYNNAA74ncUUInoYpEQd9+dLPOIoaZ6TRBQf2oEQAMZo/z\n" +
            "olEAgxQFpJMHFCMzzmiAk74pcHc5oEkDse9Nr9B7U8QADmabUDuKBo9edJIkTFPEACRSCMTOKBmC\n" +
            "KBmPanIx/SkkbmgRyowKMDPSgc+lAROaE/ajAzH3ozFED1IoUU9YoUV0FPEvFLmkDzoSYJ2ikuuN\n" +
            "m2cKFJUpw7f7azrVzD6V6iCAAJNLfvdJK90qECDjeiLJVwtHiI1hIkBcjlTTV+ASkqCUkQojp71T\n" +
            "/ilOLUNW5ntSzbqS14isgiTFFX9txDxHElwgoThKTiB+1WltceGlL6MuKwhBwAPXkKzKGXrdll1a\n" +
            "AAoEmTiP3qUzxBJAAQnWPmKp27UGjacLr/irUdKT51Rt29Ks/wAW7cjwWkfw+RVtHWOn0rKIvy4P\n" +
            "BQdQSJVJ/uauuFvykBYbKSJU444JH3EfeidJRaS6hZaJXoySkGJ5f8VFU9eqSdSCCnGlYx/+3b8z\n" +
            "Vh+G1NLabv1BRGrwyCkEDpg49KZtLC3bx4YccdMjVqUAZ5bAVFRitKm2kOLLZ1FRToI1Dp7/AKVZ\n" +
            "svOlhpLelClGVFIKlb4I++1O/hCt5LDFoEk5OlUCCepECn1KQwlDSmVkhPzInUBOJIO2OQqxZFra\n" +
            "krdbW5q1BITqKjBxOfSjuFKcb1uPDSWyoHYKOD+sdwarrN1Ny2oIbWkBJSoRn1PUECg/dLWHEwFN\n" +
            "qWDk5RBj6QKKaXcIuWS2gBCinJJkxgTPPMUrh6vxDzTTxSXEjQFgxB7+9VRXpdClpOkKgoOZSeh9\n" +
            "hRm+dtHkBaQhClpUFDkZz9/1qpv6tbp8LTcJUVJ0glIOJx1+1C0vUW4VMlxaPCOPlGxNVt5dOOvF\n" +
            "QJ0JBKhyGf8An2NQfxiUqCNScLGZid/tQ2NMi58a4cZQnCwAoJ6ckj7mrB62s+JMeIQ2pttOlIV/\n" +
            "pgjljes0w4bdByNa5JB71Y2Nwbdtt/SVJSSlPY9APzpwFXvgC3cZYLXgJEeIGAmPvj0zWF4gVtOq\n" +
            "lNu4yTPmbj80xW7Pj3gjxWbdaiYKnZREZJCdj71guK2fH7i7Wyxw5xQCvnSkKCzO4jEfWpiVWfhG\n" +
            "rxPi2ygw6nchR0H6nHtjsKqblxxLy23kI1pwZSAav7j4X+JLVxtTnDXiRBT4Y1aZOJA2zin+IfB/\n" +
            "GlWbTzvC7hCi2VpJTJxug+2R7igyOuM46xTj4DakqTIQtIUn0/5Bpx2wuGVJDrLiFKwAoETT/EmE\n" +
            "ps7JScKS0EqnqfN+poISfNgAe25qStjwBpX/AKhzpB2HekNuKstJCf8A3Sh5BHyA84/3Hl0pbTKU\n" +
            "BSrlwN/9oBUufTl70DSwVEDnSSJg71IIaUMeIJ2Jg/am1tlJEHHKRVCNjuR3oagnmAeopGdvvSSQ\n" +
            "COeKB0EqEyKQonBO/egVSkdO1KnFQNlxSNvN2pKlFW5pSpPsaQoGZ7bUBBOOVDSDzxQzGKEgDn+9\n" +
            "ADj0oEkUYPpRkZGOdAnOoZMTTuqd9qbIjE0W3vQOiJp2CEnY1HSodsU5rAIoBBSr5pn3pGnzT07U\n" +
            "6rSVZOPyol6TBBHqKBJ2ojkjYelKOxyCaRzjtQJyB0oiNWBHrRkkCigTjNAYxE5oTEdKLpjNCTG0\n" +
            "dqBZ9ppB9vaj1DrQKkjMxyoAobwBtTZCtUzSxBJBoEAY770DR6Ukp35UsjekmDOZoEjlRb8qcCCn\n" +
            "FAjpzoGt9qVGwPOj9N6EZzQJg8gfpQpQxgGhQSw2S4kASCYxUwWqEXCES82pZiCApKh2Ip+1spWF\n" +
            "KkiAcmBVo4zb2rZaXAWggoXzSP8AmgoDZt+bwrtkjUfKslJjkdqsuDtaL9pVyEKZJCVJSpJkfWmC\n" +
            "w9+MXLBc0nGkbztVxcWRsWbZ8sogpBUlQ5dx260Fsuwc4g14KlISWQUlwDJnMdgO9UV3wRy2l6Vl\n" +
            "lCtJUG4jpHbuaS/ePosn/ASUlajkJn79KqrTizjK/DdJUyoFK0H5SDzgcxUGh4cizfAJBIPJMEE8\n" +
            "vc9KuuHWwunMkstoSYDgMDuRED71jLK9HDH0rQ6hbZhSYR+vIithZXL6yX0gi38qkDJQeoIJ3mc1\n" +
            "UXzVt4jTb5Ky0kGV6tQMbAcge2Kj3b9wm+S23YIAwE6wfl38yZ/arG3eaUFwhcEFUTn1+kRSP8xa\n" +
            "TdL1LV5Bo8RZ1T36weWajWfUN124dUPGbWCswlSdI04nAHarixcWbQh1kEoCUo3UoJmPmnFO8LtG\n" +
            "Tar1uoLZVoRLepSjvuO/I7Ut5IWp62S+UIBnSrIPoRE+lXgNpvEAltt5lp9YhIMFKzOxz2Gd+oqP\n" +
            "eIYcStSx4TyN0kjRPPbIHrUZ03Fu08nStbYIXCQlQPTECKq3UP8AEHCyxcIQtRBB0htwHmQZhQ9a\n" +
            "A3LlTS/wyBq8SAhYAUCekik3Niq5YW+h9vRhIScwZke1ON2lylaPxTyXScqWUxHvO2Kebs3bgutt\n" +
            "kEpBhLJkJG0mcRVZ53FQ6xxGzUlPhIUhRIcM4IPX+lNpXxQKCTZtr0kgK0xzrV/+mr2/ZZLN34gb\n" +
            "b/0/DVKCB9x3E4qcj4W4h4sXDhEkCEoE7bzUMY9Dd4+pXjuW7apETkeXbb1pM3zxR4cHQjThPzK5\n" +
            "kE/cV1/h/wAG8IbtYfSFeJgqXKic99qaveH2XCXAGoEAlONztkd+opq45Qzwy7W346LZWkGdRRIJ\n" +
            "5Zj7VZt3fHkOgJsnlhBlSkkiO9a0caLCfBW5AcGAVb52zzp1r4ntUNL1lKXUqAcJHziIP5A0OmRu\n" +
            "eOXDYFxci7Q4s6HdSDI6ZG9Ia4+y1cIdau3FK05QtZwf7zW5SbHjPCFpXpcUhUpI3iAZxWIuOBWn\n" +
            "4xbL6I8moKGNOd5oHLnif4hptv8ADt3GkGCtIUqJmOtRr34d4dxFBfUz4RKQpSUCYUNUEg+vvTNr\n" +
            "wyzf4quxYvnLdxuIM6tQiSfattbfAHEA2kji9q6VAFBUFDV07EUtXtxXjfCBwriSWW1r8JxOoPBO\n" +
            "pxzmSSYCfQfU1UuLt/lYSvSk4K4BJ6mK7fxr/DriV5w11p523aWry65JyTP6fvWIe/wrvWFrbF+0\n" +
            "HAYCSgye9EYImFHkT0pK3SBBAmugp/wqu1ap4tbhQ3AaVNNs/wCFXEbx8tW3ErEkKg+IpSCD6Edx\n" +
            "Qc9wcUk8hsa6G7/gz8TplTTnD3kAxqbuQZ9oqKj/AAi+LHk62bRpaOSvEAH3oMMkkCTvNGSBEj61\n" +
            "qX/8N/ilkqSvhbiiB/IsGfTNZ684be8PdLd3avMLH8riYoIpM4VGKKZxSiMzEmgGXHPkbUojokmg\n" +
            "bJO8UJxBqQiwvFQU2r5H/wCM09/k3FCjWOG3ZR1DKv2oIXpAotShmn3LO4aBLtu6mP8AcgimFCCA\n" +
            "QKAtU4xQkE749aI7be1FiM0CgrePzpaXEjeMUzMbUDPI+9A8l5OqefSlFfOogwqQR7inU7UDqnBp\n" +
            "JkTSdXPeaakxFEFY3oFledopQV/zTe4OZowfrQLKu80Emd9+1IJnGKUI5nNAYBJwTSiSkQfvRRtJ\n" +
            "oEjrQAECgT1oRqTjrSVK6/8AFAUEzmjA8u9CMif3obYoCjvNHEp/ehAUe9H8o6zQIOD1oZ6mjO8U\n" +
            "NMnvQI2xmhSiBO9CqNVwJ9t19La4KSANpE9K1T/BV3KFtIQlxEHwzMlJHKa5/wAKvVW96ggxJyPS\n" +
            "uq8FuDaqU1pQpDqfEGevMHbrWasYRsLQ+hKSfEKCVL20gcvtHaj45xJIa8E6Q8ltCVlIjAMwR71p\n" +
            "vilhLPES48lKW7hRdX4cbaefSSDXMb65D9ytYEBSpg7miELuFrWpQJHpim8wT96MCeVLT0GaoAUo\n" +
            "gJJMdIrRcI4uWR4SVq2AiOY/4rPRJk4pxk6FGPL/ANw5UHWLLjCHnpZ1Id1iQnA2x7HIp1wWwccd\n" +
            "WyUpUkRKeczj0j6VhuFcX/CvpBgJJAWN61jr6126XwsIQYS4CqAog4MdfpUFkzeqtkoZfdjWnS6t\n" +
            "Hl2OCDtPPvNX7ZZDJSlX4khIRpWQULJOCCIg9j3rFONOKfQ2C6EkBTZAnTBMJkHb1ArV8Hs0tNuA\n" +
            "Ou6CtKVEGPXCjEVcNXLfD2EWxuLZp5sgfL42kCOaZkH1BrM8St7H8YhCStD6k+Z5tQcA7EAT9zFa\n" +
            "2zeS1Y3DLTaSoEgEiCJO3PcdKzd9apsbh9dy2hLbi5SdgSOUZIoGPwdtYNstXSbWVHyjJJPSQMYq\n" +
            "x4a0wlResVrLoVK2UJH657cveqcvhDbaLdhhDRJWqSVAHaTkfmaTZ8VCbl3xEPLGEqU25gdx0pg6\n" +
            "DYKbUsH8I61cNiTq8wGefKO9XC/Dba1NrWStMgDLasyecisG3xVJcQUodJ2SguEknpAM1Z2F66hS\n" +
            "lKdbSAN8mCdwATj70Va3N8lRQ3djwrgwASNhPYx9f6VA43crRZptnVHxCrSEqMkzzKT8o7ioF5xw\n" +
            "3Lyki3bWhAgrKSkT7/pTzPjXlyq1aU4jUiCgKC89DIx7GpBj7vhb15cBplL2CFk6xn69udYr4gvr\n" +
            "nhfGnrN1KipOkk6xsRI2ro/xY7a8DQOFtuttNLZDl3cAzq5wiOWDjn1rkLvDr/iLV5xO2s7hyyYU\n" +
            "A46EEpQDtJ5VWW34RxviPB2rO6USqxuG4StMZOAUmOnSpN65ecQdSpCXHilJSlwCfKCYrOfBXHbd\n" +
            "tR4JxcpHD7lXldUJLC+RHad+1d/+Hvh3hdtanW4EulMEJWNInZQ/vnRXKbXhl6i+U46wtL5EsnRl\n" +
            "SD1+1bG04zfWgR85CIKkmQdJ7fSuns2FoLdtp1LbulJSJAMioFza8IuGXUlDKVNqKMx5Tif0orLM\n" +
            "/Fbj1ufxQQSBJKxyUNv73pm4f/FPAlYJ0klxCZ1bxHsftT3H+HcObCnEOKDcJwgAkH9M1lP89Tbo\n" +
            "/DILS3iMNlekqAMA9IM9eVDUHiF1eMcSeSCpIMkOKMAjr/SnLP4rQAXlrQtSEjzqgGBiO5P6U/dF\n" +
            "HE7VYVKVLcUC4kSAQPMQeeeY61guI8PFpeuo1nBgKbEg8v79aJXQ7D4gQ8lJFqhwqSVKC1gAnlpE\n" +
            "ZgdcVpeFcXLiEpW84lak+VKFSkCefT161xRpFw2yh5pbgSiQBqmetXVnxS+bYt0FbqLdIAlIGSTj\n" +
            "PXf0z1phrsD3xIlNiVMpC0knGjUVjrmsRxlDPHnENvqRCs+EoQB3yNqg279z4DfhLUpoOlKhnpO+\n" +
            "Z33zvTN+lxpxy6abCygeZtKNJSSOXInn360kO0JPAbS3usttKUgfKhA0z6+ner5my4fbW4SsADVI\n" +
            "A1JHpjf2FY93jKELAUtSjMnUoSk8/egjiy3CEa1JSVHVklRPXeqjoNrdIbQpDCEa0jUSlMiBmST+\n" +
            "VXVrdMuHVcO2wZiTpIJT9c8/TNcxT8SqtmUN+bw1atC3EgJHWO+BvU7hl0eIPNlIbKkkL0olR9SZ\n" +
            "An3ouumLSi6WmGFKaABSleQZ2MHb0qQx8PcN4s2W7zhVqpKjHnZT+kdKybXErlS0oS602lCvPPPH\n" +
            "Un7VGv8A4+LC1pZeCVNKgFJISrGRJG3pUXhN+Kf8C+A8RbW/wdxXC7snCSdTKu0bie30rgXxB8M8\n" +
            "S+GeJuWHFLdTLydjulY6pPMV3Jr/ABEviyghj8WsmCESADtIPP6VjvibiCvi3iJb4usspSlSLc5I\n" +
            "bM59T60ZclVgQJoiMTTz7ZaeW2qNSSQaaiAB9qKKM0YmJoERFHy2oCJpAOTmKUaLM0CgYGPtRTzF\n" +
            "A7bUP1oAD6ZpQMHrSCYoUDwVjGe00UnM0gnlzoxyoFAyd4oyAec+1FImMe1FmYoFSTO0d6CjEcoo\n" +
            "hAz9KInNAsHFA7ZNJnmaGrHKgEmIo9iNu9InOOdHJHrQDB6UKIj0oVQtKglYJ33iuk/C1y1xHh6U\n" +
            "O3OlLSFak7KjoDXNFY71dfCvFHbDjDSRBQtQTB5d6hGs+In2Lrg2Ek3LytAUMgNp2HryrnRQULUk\n" +
            "4IMEGui8Xsn7m8Fg22A646PK4qNJInc9xWI4q14XFHk4MHJG01IIMyYjlSgADjpyov5uoowO2KoV\n" +
            "y7UsSIgwabBzjlS05OxzQPIJCk5yNiK0PCOIOKQbMlJK8CTAM8s/lzrOiSKkthYQSgjIgzyFUa5L\n" +
            "tyi4SbYkoKAlOv5gBj351dWnEShanX8rSAkJJgH96xVtdqRDdy4rTOBP2PapjnEnLh7QyrTbt+wj\n" +
            "0ojq3CVKccRcKUls6sBcaSfeI7AVe3V2xdpS26y2pXyiATBHVKfzrlNlxdCLcNPuktBIBAmTJkQe\n" +
            "RFanhl3boKLhB/EOhMlLi9KRG5Ku367GmNamXXDza3DqNT108oCShg6UnlHOq28Um3cDf4cpLkeW\n" +
            "ZKu+8itIrh7Vw03cBi7SlaSoGFAxy5if7io1zYBFt460uairSE4Go8wBvnuaUVTa3A620hp1tMwr\n" +
            "CUz0EiI/Op7SVpV4wet4K5ATKwmO5xPambhSUvaS0yP4calnVoHWNu0ZNVzboZach8P3JV5ST5Gk\n" +
            "8wOQPU7cs0qJ10hD9wl5xC0gebW4tQAHMxyFSLPiKD4qmHyptJ+ZKy2pQPecD86pb28Wyk20oSgt\n" +
            "6laUaZnJnp+n2rM8U40yxwcMsN5DviLcP8xIwn+8iiq/4z+InuMXrmswpapUnSBCR8oqjTxviTfC\n" +
            "3eFsXjzdk4QXWELIQsjmRzq54N8DfE3xYl6+4daF3SuFLcWEyoiYE9o+1WvBHrf4V483/wCrPhst\n" +
            "qSQQ74U6c4VpnSodxURiLRLa30tuLAbWCCqZg8j9atrH4t4zYpQ03ceJpSG0hY1Y6d67Xx3iP+GP\n" +
            "H+EuXameFF0ZK0ANOD6QT6VzD4U+F08X4rc31pZOvsMuFy3t1kgLTP8AMd4A6UD1vxX4vQtvhT9v\n" +
            "eWilI1oEFlRTk/zcuwirDhfxD8ScPZUlbXiiFJRqPmyZUO8mugMfD3GmkLevnV3SXkJbDTyipAAM\n" +
            "pA1TGMb04Le0HC1ttWbi7oLUlSkEAhCSYA66ZEc4qmMMv4m4xdWrja7AI1GQQkkCPtUJ23bK2n76\n" +
            "+buLuAspKCoJHKY5DkMD1rQcTTZs+KTbhwojMwUnbUrfnGOVYe/4oyt5xSmV6gAglIDYGTsNzJ3N\n" +
            "RGjRxZhyWrfxtRkL8U5XkkkgbTiBTTwFwhpsBBDqgdE/Odic5A9azvD75lpxBBKCEHkZknpv69dq\n" +
            "lou3n0htdwW1k6gVzsOQAAgd5qi2fZaYQnS3bnyqCVJiO4in12N0bYN25KEAB1whSUgbHSJOZHSq\n" +
            "m8Q5fcKbTbNOyj5tRwpAPIctzmre0uSh2XTIbTAbUs7gQQI32BxyFF4SE8OuFLOpxAUlPi6EJjBV\n" +
            "ueo2G3Oo1wwUH8Y4+0iVSuAdRHcD0px/iz67taFl5T4GpxsL8qidv/2jAzj3o7i/S0w44lwBUhJK\n" +
            "GyEx07z+lTBR8QQzqcW5ahDq0EBQBSYxGoflVYxwhxxbrbSFqQiFqWTEev1itI44xeI1fhCVk+Yz\n" +
            "qUUg5k8piAKntWlwGlOJt2wlc+Go+bIH+3nH61UQOF/BTzxQu/UhluJ8RxXl0+v6Ud/xThXw+tQt\n" +
            "UrWG1QnSsJQv2OY/es98SO/Eqm0m4RceABCdMwkDsMDY1j3PGcVqeKyTzVJNDF3xD4r4jeXC1pdU\n" +
            "gLmEkzFU5unlqlTiydpJqPjYe9HIIImoqa1xG6bUCl5ccs4q6Tfm+dRdSEvBQSeUk4n3rLlUGRNW\n" +
            "Vi8JDEjUVhQJH60FZfD/AN4+DvrPPvUVIHWY5mn7gEvKM5k01Mbf8UCSevKh22FGACT+dHsn3oGx\n" +
            "/c0B9YopyKVEkRQAEkRSScmaVMGkGCKACd5o5M+hoTtij05waADb+lGMbfagSaA26RQGI60alla5\n" +
            "UdWMTRAgneiAzQHvuaFGIJ7UYOIgCgSFGDmiycc6V/LvQiQDNAQHWhMUoDlM0FEYEUCDHM5oUD2m\n" +
            "hQKMSRM96TlKwpJgziKUR5qMpAO4zQb5jiSb2z4bfBSlXCT4T4OSSPlUOcxWY+ILM2vE3o1aS4og\n" +
            "kzicVL+GzbvIuLZ5QbXp1tO5OhQO8f3vWivuCL4pwkrbCS+0gKIBiVc45ERU+jnpB6TStGDO1OKB\n" +
            "So7CixM7VQlKBjGBzpYSAdogUoJ50YAB81AJyJJmplt5SFlM523qNoiT+dT7BCW3mlqJEEwOXrVF\n" +
            "ha8NdunUlJGkqCSTgknpP3pbHDXWrpPj2i3mgojQ2Rg9+la34dYt3VoK0o8ZI/hpUTkz1Gx3rVL4\n" +
            "K3cy4GkhRgPJKwNt5g+YGYziqKTgA4a8Q5e8MabaWCguhPMDqMdN6uGeGBbzTDK2kuLJS1cMNApk\n" +
            "T5VgnBjcZmrKz4YiwUq3sXPDQ2YWlS8gcgVH6TvtVl+Fa4a06tClAyCUkpDbhneNxHXnipor2LB6\n" +
            "1DTSEKbcUEqUhKyptUbqCVbDJwPtFB9t19lbbTLAS0IUrUQkY5ycDH0qSOJ2l46ppDv8NltwqAOU\n" +
            "nBOecZx0NVlyhTds2hl5AbuUFZkkEnEpAzGD26moqnBavb0NNq8RIEKU23pSR/2g8u5gVKYsU+Lr\n" +
            "cYbbaK5GjJxtHUA+3OTT3Dbe0YYW2hIceWqQmcY6xOP7707+H8O1W4ZW4qYUTkx65A+9WVDbrFo8\n" +
            "SFspdQlRUpMyt2OWrpNUbvwhaucebv7kLcYkrS0YCCQJMdthP51btJDAU7cl3SoSUwQFAZgdBVXf\n" +
            "cbZWlOtqAE+Gnw/5jmTFDY3tv8WcM4Hcsm04TfrtlM+GXUJSEqKABhMyTAjNNPfH/wAG37xa4nwe\n" +
            "4bSoHU5cWcpA5TE1g7C+KleCpJ8DKdC3JhPMn8p61YWbNo/4DTC1oKtSY2KvUZGAKYjRI4T/AIUX\n" +
            "Vx+OS1wsrJ1Nth75v/8AGCY+lbvhb3BrHh6Rw1llpgDyJaRGOU9J71gLPg9gywppdolmJWlxDRSt\n" +
            "AG2d565q04VpShLbcMrBlZlRUrTvqjfH5zUVrnrs3iQEOBqMFs6SZPyk9OftWR41wW7S286lsmI0\n" +
            "hKwsRtkYJHvWh8Zu3sPM5OowFIA0oVvHXqMioR4i46Up/hhs6U+HA0mefUT370HI+KWj1s06l23K\n" +
            "9cqkiYOB7f3NYR/Up0FSANBmSTn15xXZ+I2n4u6ubVq4IbcEpRkSU7bjePSudcYsWEB8ot3NaRAW\n" +
            "oFIMc4O0gx0xVRlWnFpWBoTnIKhpHpmr3g1oHXii5K9KtIUELIUUzkCarkKLdupKTEiFGJKR6dKv\n" +
            "OEqAuRcxqShKVjUqVA4BMxywY51BoE8NatA7+FubsIhSgXW0/wAu5nryIOOlNcWYuGmZKgl1AABI\n" +
            "haZHbtHtvVsLd13hYLV4jUFBaACQJKtICd5EwYyY6imOJtJdTaqfUhsqIC4+Un39BmMVVYlNu+4V\n" +
            "XLzy0lEQtJmBt+WKsbFwNJQvxNS1aknxFakhQgYB2xPrVs9w8pbcbatfDjzBClZWB0PKah2dpovr\n" +
            "dKUqbQrzScgE88jYbURacOZW746m7cLCk4Tq8sbTAP8Ac1Y2l08VKQh0rUR8ilhe2dJ9IFWFrart\n" +
            "CLZlvR5iNbhgJII3jlHrk8qceDSS6Q22xpSSU7JQo9zP16TSL00Fhx9KbS3tr63CGinzKSIgDcYx\n" +
            "Gcf1qt418F/C/wAVrcdQ4i2dyUuNeQknqnbHXnVFaqav2UhXit+IsoTInaYUJ5QDmkutLtHtbTsO\n" +
            "FWghJjUQJKpnemDlPxN8H8T+GrtxFywpTBUQ2+lMoXHMGs/oURhOa9I8O+LbW1tRY3doq60SrUka\n" +
            "0zjadt5nqDWw4fYcC4taNkcCtvDVCvMyiMneg8f6SUbgnaBvUzhSEucWs21QQp5CT6FQFesL74E+\n" +
            "GuMWjjT3BrdlZBT/AA0hKk9DKa438Qf4TXnA+KJctbpP4JRlpxQMtqGQDHLvRGM/xA+Dn/hHjpa1\n" +
            "+LaXAK7d3tOUnuKyG3U/avR3xJ8PP/HH+HXiKQEcVsT4qQpUeaIWk8thM157fsVsyPFZXHNp0Ln6\n" +
            "UqovvRqHlHPvRlBA296EH2qBoJkGROaGmD6Uojv9KMJ2E56UCJBpKgDOaWsHpiaSRuc0BQR6UYAj\n" +
            "vQjej7waAKHL9KAAB60Zj3oTJEzigIDNAGDQ586AJE5oDjMnaiPOgDiaG6p5UBYpQI5HaigQcUOZ\n" +
            "jagBOCaIjmdqMgjBNA/LM70BYNCk6uhihQPK6CiGCKISTzxSgMb0EizvHbK6S+woBaczH2NdO+F+\n" +
            "OW13w0jwUl1CwFtATuIwOlcp2g71YcK4i/wu9aurdQ1JMEESFDmD2oLD4m4YeG8aebSk+Co60GOR\n" +
            "zVSkCcD610PirVt8V2ibtLpFw23pSSokDmAd6wK2XEOKQUEKQfMDy9aBOkTn6UrTAJAxzpJ5dO1K\n" +
            "9qoUgAGpbDXirACikDJKcYqI1K16B8xMVreGcJCgoIaW8hKkoJHUAkjr0+tAXDuJm1Wl4DSERAOc\n" +
            "bR6H71cWfGL5txTiGtdkrCnikq8MQTj/AMR1xNaZn4XbSyw34ai44lJcKR5U4zJPflVH8SWSLJ93\n" +
            "h1q0WVJIBEETPI8iDQVF58TvFTCUlRc1Jla41aR1j+4q/wCFcccu1OPuuuv2zIWFrSsBSU7Agbc4\n" +
            "7TWb4fwa6ubi5ZMIdQQlIUqZzGPXpV3f8INi8W22kpCFFRabVCoidRB39RPTFP6iY1dIbQr+MA5I\n" +
            "8JSZEpmdRx+vXlVjcXDd+8VKacakANowEiDsD0PLAqnt+JPoY8MJQhQcny+UhJwfNExPIme1WNpd\n" +
            "lF2WSwPBKQtTje6yeYzpmfequrq3UhTSmJKfE06wz5SB/tKuU9MT7U9fs+EQ1aoU2hPlOk6iP+1K\n" +
            "jsMSTH1qvt7+VKcdcWhlAIVBCQhMTy/TJPaak3ZTd8PUpDigqMBwalOHsOQA6czUFPxC4Ky63guO\n" +
            "GPKM8/mJyB9zVO80lvSgoDpkJATOkADfP971Z3CQ/wCCzqDKAAFFZkZIkmeZx1+lJuLJtdsG2rhJ\n" +
            "cAJcWoxGZBk7x09KJZyz92hxDhbdhP8AIrQdIJ5QnoBzqZwbiTVkpoFxKF+J4YWrzlIIExH59qr7\n" +
            "+48IOMoKnnChQC0JKJ5T3wedZ9S3mFlMaULxomcDmSO9B1mw4olLrajfafFSsBttZkZwox3/ADJq\n" +
            "XwzjR4feJC1yXiSFJUSpUlQKuxA6xArljfF1WqNSZKm0+GhZMK3k4B5mrGx4q+6tKluhCv5SmQkY\n" +
            "idPMA70NdgTxpl9L9sFOKbSBLqgQTjc98DO2eU1QXfEfCVbuML8R5QCVaVeUjYBQ39RWRVf3nh27\n" +
            "iFG4cCFW6ihQI0jzQRtEGN6ds+LXqHGlLUhDqzo86UqJ28xOwgbczG0Cqa0bPFgWFpeUlLzZCSlS\n" +
            "jq9B0rLXi0qUVvOuKC0LSnVEpAM5HXBo3X2vxrzq0oeUtAcRBnUSc6j7/lVRxa+D5W4tIw6Z3+X1\n" +
            "6xUFW6hrWAklCsBRVJMTv9OVXXCGlFpFulHhrXs7MhUR5Y/uIqueDSuHOuJMqWAEqUcgajv9Imne\n" +
            "ApD+ptZcC2HEONuI3gK80DmY5dqDoTCbVXDLhl1CyyFBTRUsFSXEmSnSMnGfpVVbMuvX6VgF1hQC\n" +
            "XFKVpUSQczygTkGo15xS7eRLI8FD69DoWBIMAFU8yZ+vpU4cUb4daIQ2UB8LCEByJJUM99ORigX4\n" +
            "TLd5cOtCEMolZWvUSYwPQTn9zURy4dSWkpSkuvJCW21JBIk5xz5nsKruOcWVa3DNtauF0kansQFK\n" +
            "iCI2FQbriwc4g21aq0oACC5zkmT+wp8HROHvWSG2LNy6U2VJCNIGkpXMk55bDenvxtktl9nw2g62\n" +
            "4FL8cwkggCc40g/frmueK4s3dlSrduS7DCsFajnsIg9J5CnRbXtlcfiVuF0IOgMNGAlROnUqMmOU\n" +
            "9KGr+4uOIW/Em7dBd/Bn+MtbiNKIJlISYOw5fvV7ccFf43eWXEReMsNow6wlgiRGVTzGYn1qDbcb\n" +
            "S60lF8WGwlUBZhWkAQUxzM/c1YOOpRbptuGNLdUSmClWG5GEkxgyNuU9aK1llw/hdq820izUXRHi\n" +
            "AwTpA+ZUnHWPSjueM8PUwpTb/h+Co6g0dM9IG5wfzNZP4YRd8YYW1xBDtoXVGdBBK0SZM9z/AFNX\n" +
            "XD/hixtXbhxaHrh9WoLedVGoJM8+v5D2oLEfEtyhpVwzboWzBISE6ZPr61fLQeJWbrL7WnWn+YTE\n" +
            "j9KrLLhzRSbhxKUtJBHhlQCY3Kj9AParli5beb1ISoeWQVJIBHUTyqCJZ2SLW7K0iE3KP4iYwVDn\n" +
            "HcV5m+Pfgx7hvxXxBhN06tJcLjYcRpBSrICTqzExtyr1Iy8Xy4sQUJVCY+9cw/xr+GH+JWNpxW0X\n" +
            "bs+BqauXVplWkxpj3npvvQedX+Gv25heidoSsGoyklJyasbvh6rUZRcOCcOKa0pV6b/nVetXIzQN\n" +
            "HOJoR5aIbkSRShMUCZIHQUnJJzvS1DnNJTvNAnmdqMb70YEzmjkRBoCIgYik4jnRkEmgAMUAiQNh\n" +
            "3oRO9Kgjek55A0B4ouUcqUPTFEd5NAR2+9DkYoRijBOnNAlW4JoEyJO1Gee9IIxQDw52I96FKjpF\n" +
            "CgcAmAJolb4NK0yfakqBxGetAYmJxSwZPKaJKZG+aUEDrtQaX4Uv3G7osqcbSw5lfiL0gRgHetBx\n" +
            "7hCeLpfubPwG1NnzJSjT4ijjfY1z9vUkEpNaf4f+JnWHEWzy1+EohJlWB3oM6tpxtSkqSRpVpIPW\n" +
            "lNILi0oEScCa1/F2eE8WvNVjc+E95lqQpEIVtMnr+cUzw/gSC7JRrQowdJ29v0qppngvBiPDfWgA\n" +
            "oVr0kRz+5wa6PwZm3sfDa8fw4wlARkzmVHrvgkVXoQ00whKgtWlJDa0ADT1nczRO8QQ8wLWztA+p\n" +
            "ZhtxZkoGfMTtG4HuaK09xxO4s3wta3k2rqglsJSFKUDseoPOTjaofxBcNJ/DKub/AFCIOpKApZE5\n" +
            "OJGI78h1qoZvWE24VcXeh9AKFuIKlKVHIY64JqM7ckNuruWl+RUeVAOqeyt9/U9aKt+G3doq7Li2\n" +
            "lEJSQhxJ0Ng7bDA33FTLqyWX2LV5SZUSUFTqVKif5SRB9JqmZ442n+FbIIZbUk6FAHMZKpmAOgzW\n" +
            "qR4HELNtu+bQ1DKXEIUoiF8jHIGSCMelEYK+aumuMOuOOeM6DrJCYBAGAeh/Km2+Kq1NBx4ksKGN\n" +
            "O+raTIxOO1aHj3DV2iUvLQfDKSlJgrMwSPNzzIgisBelVo8EpUspU3hQSMyZmkTG2ZVbP2tykaFF\n" +
            "MEkSdaiTpieX1pvht8ptxTbbikPa9DhnkroPSfc1nbC+adsFtNk61NgBKzICgeX0+9Sl3LyLkMJW\n" +
            "jS0kuFJbxmD9siKDRN3DN2FBy4TbqKimckqBMDEZ6bz6CluNStu3eLqkuCSAZgzgnoD7zgTVGxxU\n" +
            "+NbPmFoQVNDUBEGDI5TkZ7CrDhl6s3iAFFC0FMuJX/KDpTj9aSim4jwA+I6S8pnQor0xnSdpO2d/\n" +
            "aof+VNGzVpIUtJJLjZJBmYnp129a1nFeJPcRadDaG0N6v/jVKnSD5iTyBj2E1WXziUrLyISYCtIJ\n" +
            "kECAJ9x9aDGt8OUp1w4HPKTiPQVbW3DnlnwyJAgAqTHLbO3LPfvVpb3KXEuLe8RSgSmVKws8/qfY\n" +
            "RRWzD7LSlreKVOL8jedQMSPU4oEtMlN2bZUpA86UpMhfl39O1RltNm4cdbIR4UEAgqBUe+2P1rTi\n" +
            "3a/Ei/0oSFJIMDBMQZA5ZV71RXvgNOnQqFOLMoJwI5mgjvqQjQXUR4fl1SU6ZBGw322qn4hC1Ot2\n" +
            "7mpuAoSrnABMevOp/GnlPlDaVBZfIWpScg4gRz50w5ZBtZbASherVJGABvt+cUFVrc8JbZgEeUrn\n" +
            "luR+VP2K3vGU40soBQSSM7mPbE0V8hlF4RbytsEmD5Z6wOQ/apVq5+HttK0EqcjUAMgASkfU0Vac\n" +
            "OUh640upceSwNSwD5dMYHfPXvVY5xnw778VcKDymfKlKsalHE+gFRLK4u3Tc21t8zqf4zhMBCRmS\n" +
            "TgDFMr4nb8MZcteHaLh51JQ5fONeaCMpbCvlG/miTyikqQy/xd9d068FnWvEwBGaaT4z/wDEUo6T\n" +
            "/N+ZpxrhXg2f42/JQ2pJUy0k+d3lPZPfnymmkXRADmnCRCU8o6UGiseIhhTLQaQhCSChKRpVMghZ\n" +
            "Pp1q0c4m4dbKJLVwknQhejXGQCrcCBvmeVZjinEHGeJuNoMJbGgwPmVpA/OasfhW3UniTXELpRLL\n" +
            "StRSVcwJAJ7bmg0Q4feXCgpuwS26lIHirGhtsq2GnfGSdvfc2llxZfDEfhFXaLpS/wCNIVpSEhJA\n" +
            "UZiB7DcVMF9YPs3FshbjxfCg02lMmI1AkE5JInpG9UvA/hNz4gU6oo8ApWVIcXGc4jPmmMbCJNBr\n" +
            "LL4oPFGlNWTTZSlKUiG1EJzJJUCBM9DsM0G+MX93ePJV4wQ2NPhgApXO2BuSdh671ecI4HZfDNqp\n" +
            "L/gvXbTai60gf62r/tODk71O4fxbhPEVvTwtTTlslsuhbYJ9AB0kknvRU2yTxF4aHXVW/hfPLKdL\n" +
            "ogRABx3H0q3TdsodLZcQs7KA++KzN7xFfEH0pYHggrSVD5SlU49QfSKmcHtgi3KnSULaAKjrKkkd\n" +
            "c89vSmBPHviux4JavNPPtpuXEDQkYJJnl7Ua2WPjj4GuLW9QCH2ymEHzBQyk9jIrgXx/8TucX+Kn\n" +
            "1JUFpZWUJWMSBXRP8JvidT6TauuaQFAaSrGef1qDhvE7b/Lr163St5tSFFKm1oCSkgwQYUR96qXD\n" +
            "qJNdd/xU4Y9Y/GF/4lxwlFq/pfbTcsJCkgjMEJ1HM7E1yq5QhLyglbakyYKJg+k0RDgyMxS4jHWi\n" +
            "VknelpTiaKQU5j86SBpJNOEQe9EBB/rQIAI/ej1TuM0AB9aWBEigRic0RHTY0DgTmZ3ofrQBA6Uc\n" +
            "bYBFLAEE86GkEg7igR9KImQZFKKY32oRPpyoEbZ77UQn2oyD8tKAif2oG1QPWk7JBpZyd/ejInvi\n" +
            "gSCI2oUk+poUD6ZAn8hTgQlYGYPamR5lZMHsadbOYJ96BQRJ6kYpegEQaIpEfMZpQMgcukUBhOCJ\n" +
            "xTrDRJlIkkwB19KYKlDmaUy4pLgUFQR0oNwbO1t+Eo/EcIR4SAFlQWNSlR6/nRWL6mBEKSpKOYGF\n" +
            "HMHaYEz6VB4Qyb+5YaWwl1Ej5jMR1I2A3rodrwazcQoK8uhShJIJUZG4iYomKLhts/xxks3Z/DKA\n" +
            "8QK1aFrbj+VI3zuOhrS8P+CkG3aT4x8QhSwpsAFRScEiZI7ZioV1wUWTjVzbtuW7qwUF0ElxQPaT\n" +
            "ywccqvrfi9k2FMO+V5tAWOhIPKYzECqsihueDpYum2kouEuLUQ2opITMyrPLffnFMq4QLriBDDjr\n" +
            "qkNwpbkhKCDGCTk/tVt/nDj/AB11NktS0EJ06CIUQcifQ0dvxZq54i8lsh1pDiltqQvyoUMR3Mzy\n" +
            "zyqKhtW7dhqabPhrGVr0kqJPPG3LYE1KLiW+J27bWvxkaZWtPLEqMzOevOoXFGHeF3H491aXS64V\n" +
            "NBsmAIGCDmc+1M23xIDcXCktFJbUlQc0lZTEaUnMwMwOsUNxuuK8O8XhqFANvKQsyogAyomSQdo/\n" +
            "WuQ8ctn71kuuW62ShRISR/Jv7DNdJ4BxZjjD923fWz6VLKSS45ClpjE8s9tjUTi9m5ZpFs+yi6t3\n" +
            "XTCUo0qkGYMxyg9KDjKCbRw5OVQTtInnUg3irm6bDxKkBAJGBjqavOKcJbZu1LUFBP8AMIHkPQ5+\n" +
            "9VjvCyyDEqCyVSAFagOQ64k0ZEb0FYtj5WkAaPD23me/rVjYXiWHvxaCQogpU4VScmQT02+1Z+5s\n" +
            "3W7wDWEoUnVqdGmAT0p22uvwi0rSmSswVY0r7duef+aDUscUCrN9JUltaAgN6t9JmQD3AzjnFOE6\n" +
            "m0rCnHCUkCCIG8QN+nes01cF9SipEuafLqHMCADPX9quOH3+hUpfDKVEuFCgVAmI33k5xz9qo0X+\n" +
            "XN2FuFMK0JUC4oEwpJEykdIxjn706ho3jiSm4UhpIJUpQAcMxJn0AE96sLBhhdh4brrTpdWlUj/a\n" +
            "ofcQTtzqJb2zDbhYdeUF6SoBDcKcBxEgxmD9BV1cVN5dnh9sWZWULGEBZISCZmeWP1qiuVJdS8tL\n" +
            "kKWmASTyM9PrV58UuhTR8Io0OqHho0+YEDIEcsb846VjU3L+hLbYUp4CEQmYB/sH3qFT7W4cLiUA\n" +
            "AsNpAUs40Dp6kyal3iiFpuUrStIASlKFbY59+fvRWNsIH4tCVLWsagATsOQG/P3NQOK3ZdJ8NehC\n" +
            "JSVKVImZhPU+m1IiEpxFsfGWsKWRsDMHvPKi8TQx4ty8ttteQhJ/iuzuR0Hc/eoROSsQANzyH9aQ\n" +
            "lA8RSiklW6lKPKipS7l+8Qm2bbSzazIt2tlHqo/zK7n2in2LVmwH4q5Z8ZcS0wv5VHqrqntzqMzo\n" +
            "BkTpTkEjnU19wLbAXlRGVEyR68zRDDl05cpdfu3HFuuEFS+WNh+w2FR3G1BTS3EHzQqOg5A96W0g\n" +
            "vOMsqQSCoeXqP+KWta1vuLdQUq+dUnYHb32AoouIoL3FLi4CipC3FLSo5JE9Kmt3twWDZI1JbRGs\n" +
            "pO3YDmSf7gUy2pwMtONtgvPKKGEiIB5qPoNu8nlTLKPxD7TCTptk5UsYLg/mUO0Ymqi5teIXPDUt\n" +
            "XJw66rTCjIbZVIIE7qUJzyT61qeA8Qftr7w7lxSktBTK3VkmFyAhRx0yByxXN3bl3iF0ShMlRhI6\n" +
            "DkAOQAA+lWd3xF38AyptRhbJQqSfMtJ0lX0/OoOgXfxe8m6Sh0laoLZU2qSAFSEyd9x9a0HAviBK\n" +
            "bXxiogOK0OGACZSSVj06d64nb3S//bIWoQoqJMmem/tXR/h+8Qm8ebcKUalpU2onCBoBJiMwFfQG\n" +
            "qfW1uL9Cb5i2/hzMrcQdWokb6ukDP6RVX8YfHauH8NcZ4a+ly6c8qFKgBE4O2w9azfFfiB3gzbuh\n" +
            "S2YWrLIC0OKBORqmN65o7c3nGL4ai46XVgaUmZk4ApwFtnhzd4tF3414VfM4wdMK5xM6vXE1rfgN\n" +
            "LrHxMyZWm31fzDTHMTNXvwj8Bs2F0i7uy4h5DiTauo1fOT5R/tOc+aAYro1/8EM8OYu7+xU69eKS\n" +
            "SErUlA6qgxCc7EbTzqDmX+Mlym7+J1lpx3QlhtBUbZGhSokgK+bntXI3RCjA+0V1Li5s2Rcv37LI\n" +
            "eX8zTL7jg9FLJBVtyIFc5v1uXCy5oShJ2Q2nSkDtQitE6u1Lk7n6dKLIVkUaR7VFAgEyRREZ2p0R\n" +
            "ST1iRQNwQROKMCOh70sDpsetApgDrzFAgpxtSCCAIyKdgx0xRKANFIBVShQnEET60Y+bIx60QI1H\n" +
            "ahBjaKXAAwKEchAnlRTakxtj0pJ9PvSyBMAx3oiPbPSgbiRRc5ApShkYoYA2ohExymhRqOcChQJB\n" +
            "gZpzVOQaYG1OJBnJ250D+qQIpQPPb2poQpIp1IBIzFAI94qbaM+Nr1OJbQgSpZTP0jM0hm1efWhD\n" +
            "SSSswkROo1oUcFXb2bFgXWk3V0uVlSsIH94+tBf/AOG1sy7cXLqfKUQPFdGqBvMbDMVq30G3dTbM\n" +
            "qUpRWtxThgHT2Owz96z/AAIM8GtXOGuPJISoLUtMSsmcxk7bD3q6s+J2qrpxDyUtuK8rbc63DORF\n" +
            "FSb3UxattreLToWApgOElYVjkf6/SoXEOEvvob8IhCEBTi3NHm0jOlJE5IxVXxlx4OlSULS4sgNi\n" +
            "I0gHJJ6YFSnuPrQi3ZAddtGG/wD3DiSQlawYnoB0oItiWXlq/BrW28qI88htMfKkxPLzGMxirDhq\n" +
            "mrFYs/BtVrfWVB06gkLmNxBEDlVL4z1zdlSLltgqSS6yg6QsRtIxqzgdafU74xSXCGNAKkF2MY6R\n" +
            "9+WarLbXdtYLtWGWV2q22lH+GFFSUn/eZwZgVhuMLVKbi4DdsUKlplsaUrVmTjr1mrS14q2gFItl\n" +
            "pStegAGEuACdjjqT+9Z74tH4N5ptoFxnwzpcUomVqJmJ29OVFq94V8QMXLLafBDyVrKQYBCD0CyC\n" +
            "fY1vUPWnEuEttv8AEENJwhBBlLf/AGgj0rivArYKV57pCWlHzM+GVz2jb0Nbmw/z1hbXh2H/ALMq\n" +
            "HhhBSDp6EcjUqw5xr4ea4i6tKNSNIJWQSC5pGFA4BPKP61SN8JdY4e34BUU5JQACtB0mD39B1reC\n" +
            "4de4b/7qyXJCkhRwcHbzZ99xVVZcXtF3CbVTDyVOAjKpKlgYidtx9akMZRfALceNaOIWH2kkqVKR\n" +
            "MzGOsVmbzhZs2mkOwtpUqC5x3xO4rbcXubp67LP+XJWQAGXhIWgAiMDoR6TIxNQFX9tfcTRa3Nmt\n" +
            "duE+ZNskJ83MpG3PPOqz9Zq1tlMPBTawvGkEzBHMe4nfpUl4fg3wu3CfCUsqZKTqChsZziZM1dOc\n" +
            "ELBBt0qVbuKKUJCSU6uWeR3rM8QZfauXWLhLmsKDqUK5bk+vrRWkRdpfsGXWQNVuFmArOnkCBjBB\n" +
            "I60y9xwN3LXEmRpQpBLrSThLgSUiD0k46TWdYunNRcQFBtafDWIMwevuKHgu3DLTYWCQAognAnYR\n" +
            "NXUSLniZeQ5cvOeZZ0JEHKTuRPaBNPcOabfWFvEtMhKTJ3cIGABvsDVZdtpCG1N6VTPLAPf9qkID\n" +
            "rjtu8XVa0pl1ahzB+5iMUFle3YStTbnlaQBJbP8AqqgQjaDn8ielU17cNupSWkalx5ljCU9h17nn\n" +
            "R3d65eupCUpCW06Qdp69hR2yS6mQBpTBUdsbVBETbxJKdhHPf2pt1tSDsqd4FWLpOmQJBMpAGAKY\n" +
            "cZSVhJUEhQ805j1qqgDxEJ1RA67zTqHVkhaklcGYKsT3py6KStSUQQCAFAbjtTQVqKU8t0n9aCZw\n" +
            "1yOIN3Lv+g24FKJE6pOBSHGFfjVsDUVBzSY3KyYAFP6yxw1kQJUvX3Uo7GOiUg+5q5+GrU8U+I+C\n" +
            "vAEv3V4ltSlCQNJ1KX6wB96iJHH7FiwbW6SglKRZWjfIIbwsxzUperO3zVkDcKQy/oWFLX/CKus7\n" +
            "gdgMVb/Fl8FcWfQkKSG1FpptZy2id8bKOT71QNhKBKxqCRhPU9KphaR4TKUJgOvCBy0o5n3/AC9a\n" +
            "U48FcP8ADE+V0kR0I/pTaSrS4+s+ZXlBjb0/KktCQlGPm1H0oqY8QldsU/8AwhLap5bE/ma0COI+\n" +
            "BbWz5MupQpChtqhOn9BWTaV4yXtRAJVq/Op9wVLsGCkAJcJOqeeAfy+9EDil3ccSuW2kalkwAhM5\n" +
            "Pp1NdV/w2+Ambmxaurwr8daSoaHSNKTyMCQfQ7VzrgPCFXLarkmG2yNZPOcAe5x713j4OfbZsQ2t\n" +
            "bLZLCXf4flKRERG2ANqo29vZNNMBNshDPlCAU5wB1mpiEkIAUdR5nrVbw5SP9RSEJUsxqSqQrG4x\n" +
            "iasp0qkkAHAms1XNf8QPg9J18U4cEsOEEvFDGtSj1nEe5Arz/wActrxNwV3DVy50cuFmSOwH6V7I\n" +
            "uGUXDC2liUrEGvPn+JXAGuB8TWW1XjxdTrCtJ8g6BRMAexqzkcbWheZER2ohM9op64K1LUSVnO6l\n" +
            "TNRwYGTFQOiJyR70qekSKY1HNDVI/WgcPaKIqkZMmka8UZVM+lAYPWDRGD396SKMERQADzbZijmC\n" +
            "P2pMwqi1dqBzofelA7YB503qxnOKAMjlRSpHOh7DpRSCDIk0QMCiDIkbfeixzH3oEgDaiMRvQJwJ\n" +
            "A29aFHHT7UKBsCnADpHU0gSMRTqIiTmgCYSZmnQE/OoeUZP7U3pI3Io9UEYxQav4YYS5cLunklSA\n" +
            "mFGSNA5JHrz6AUl24YueOlwBCWIUnPl2Bg1VNcSct7bwkpIKhI/c/Smrdp27ukAq0lSsmgv0cSQl\n" +
            "zQplt1rSEKCDoJzkewitLwv4hTZhx5YKX1LKipIGrSMHfttzNYd1HgaVpUkASJPLv60hPESLtBQP\n" +
            "lOlBXy7mNzOaDW/FfEHmfGCgQoakNpTMQY809I+9U1rxjw2LdD6SFMZmMq6AxvSDcOXz7inVJUlh\n" +
            "GkKWZAMADefp1mos3DKg6pseGcICxIgGhV6zxUh5BS6ILwSdOACBO3c/lUu+u1uLaUtxLi1EpIVP\n" +
            "l1D8qy4vHGlOFTKfMs6AhOkJV1mnV3TjDjJK1FxLYBVMhMjMdcUGus711Vw0xcrOpnxIKcJkAZj6\n" +
            "iqx/XxLigQ4n+A15AkmSSRsI9Jpfw7xhCuJfjHU6g22pwlIlaeW5359au7FBWyt5a0NqWtSkuJT8\n" +
            "4O2n1P5UMU6Nds04q2Z0PEQVJIUZ23/v9mrq8vG75lbD5UhTSHHAZKSo76gcED860bq27pbZbZb0\n" +
            "AgOhtUQOn95360w8tdmlL7vgrdcQfBUUApZTEFWnYqPLpNDKfb+IL95CEBVwtDY1Ss4cM7pC+QHI\n" +
            "Vd2PELW6bQh5kNOpOpMKyNpnOJ6GcVnHkH8EXV8QcCNBgoUVk4kpBPynGeeazz13fW5ItErtitMr\n" +
            "E4j9+vWg6Unhj7zLi2r1hvWSl1CG4K0kidiRJ9RPeq4fDSi2GUFm2QBlwSpWDIMGIiPWs5Z8Q4x4\n" +
            "LVyu60OJnwgFQk8p27bHftWs4bxDiHFAhL4KVkSlKlAQYwSNvviouptnwVbnjXI4sgMrTpLZyF9T\n" +
            "jb865j8T2VzZ8WdbGtbIlKHgmUkdug7E+9dKtrlr8QkuvXDaEq/ipLZOmO/IfvU1zhlsuwS80pDy\n" +
            "QYUlQIKke/PtVMcXslI/DPtueImNnNOBiMx6/lTbyVJSA3raTogOLxvuSe/QV1a5+HEXyS8zbKBS\n" +
            "nYiCmOwE/nWM4m3b2jxDqvOiVpbOVRzIImI6dulExlUOQAhJUlsKGlRwZ2JPvUxxLwbAKjpdACNJ\n" +
            "33Bj2xRrDqVa0OLeCojzeZI7jp6Cn2HyS4pBSjwkRCUgnPptREVu2QhRWQAEgkicgc6motihtSSp\n" +
            "CErUZTM8tz+VLs2kHW48NRIGE51GevtUjLrWlIQlxOVhXMCM+g7dqCFdNQ4nQFrbKtKJ3J9Bn/iq\n" +
            "+UNO6iAkAGBO571e3TiVWTi9KBcN+fUqQY6dJ/rVO+yhbiVJUkt6QYj0mgjrS2oLSDvtB2ppKEJd\n" +
            "AOIT5SMnO3vSnGfCKlggiYUAI7il2ZCrwuEj+EgukkTEbfeKvCp9zaBDdmHHAG2kEOK5DSohQB67\n" +
            "Cr74Yu1WD7V4NKEuLt7RlCU/Ktatao6kIEHuuqJhKr7hS7Upl5sG6ATkrBGR9AVVEvOJKZuuGKAI\n" +
            "bt3hdRPMqH//ACgCiKm6VrvXzsouKJJ9TTRWVqgQANpqz+JGE23xPxNpAGj8StSANtJOofYiqpJB\n" +
            "nb2op0rJQlPIUSiQ2T/MoaRjbrQG45egoKTrWOQHKgUyCkYnPM1b8NR+LZcsymVkhbcj+bmO0j8q\n" +
            "htsHSDEgj2iaurHhikNJeUFIl0aTG45/rRG4+B/hy9c4eq6fZhhu6CHW1+UAJHTmdUVv2rAW7jLt\n" +
            "qygoKlA+UgoSPlB6iJg1mQ6/bcPsmLSLfiDjXzknS7p6CfmzOcGaesuMnilt4f4tNpxFtwlLSTpC\n" +
            "swdPLIPynnVGq/zxOphDOGlQttbYgkEwRHUcxVw7xVK7VCgpJlUJU5GlUcp5GsAhN3bsurTcLSQr\n" +
            "DS1mARyEmYPSobvH1PhxkNRdKOoJUfK6OeP93Yb75pVdR4XxVb2tFwAlXIj+lV3xtYWnEOBPhxgL\n" +
            "cCSAcgD3rO/DHErN22SFLVautKIKHJTB6dPrFa11aL+zUkI8ZIBBRqgnHaag8m8a4cbK6WlCQACd\n" +
            "QmRVCvy4EV0j/EDgybG5cX+BVagqMlfPsBy9Sa5w5vAkgVKGycwc0J/Kk55UJ57UBlWw39aGrrSC\n" +
            "SDRDHPFA7q6UCRBkU2VdKB74oFBWaAIPX60jaj586Bc9frSknEHNNg9TiiSd6B0KEbfehqxNNyet\n" +
            "ET9qB4zGIoirERFNFZAA6UNRJoHJNCmwcUKB9IxuT7UoY3GDt2pKTpB69KWFSMRQCZiImgICgSCR\n" +
            "zFFEHmTRgQc+1AYUpxZWs5/KtZ8P2aU2rlw46lLhIDU8upx9Ky9sz4tyhvJk57DnWrbSpvwdaQCn\n" +
            "zJCeu4BPKMUFZ8QPJW40hMqKNzEST2qlkqcTJg/3ipnEnAu4UoyYJGTzqEkkKChvMgdTQaWzftmv\n" +
            "w9u7I0oLi3UyoSc5T2EbVa/hG32i7LSvLB8Ekg+24npVA4ENOqSlwaimAoDGclJ51Mt7t8NlJuyh\n" +
            "SSlUpVGJ2HTNQPu8OfZLqghLoST/AKY1RHICq0qUlBYlJKpnTCiaubi9eSFpWt1LbgAXCcLG8Y39\n" +
            "orNqugt8KRKSVgKAEBXerBaWCUW7DoW/pDiSR16AVuUuobQ1blAIDCVtlO4gHb6/asfYWQu1OAzo\n" +
            "IKNE/wA3LHrzrcspKmbbw3JToS044f5TEyAeW/rNSrFxY8PW0HGVBa2y2ogoEhYIH3ifU1R3LKn3\n" +
            "m0pcbab8NAWgJyhM4MHlJFaWwuLF1g2aXl27jSlBOQAVadvf9ayXEr5NrdIQlpDv4ZR1hJBCwd4i\n" +
            "Oe00XUu74G4m2UCAShxJ06snPmPQc/rTF3bW/wCMUNDifwxBUW5VvkjuRP0rQodFzwi1S8l5bxSU\n" +
            "wo7bnlvjamhbLW2nw1BLroSA4hMhKTuT3iR6U6RkVtcSuF6SUzrA8NKQUrI9OWd+1XXDeE3XD31u\n" +
            "pfcWHEkYXCcidBB3BAONqubxm0sbQIBhsNpWVJTCkEEkFXb+lS7e2Yes2m2tYUuVyRKQQN4x/u61\n" +
            "TFOniC7a4ZumbhxdskErafX8nSBzOf0q+4LxRi7t/CdWo+Ikq8K6EgAyfqdu0Vj+J2jrNuhu2bUE\n" +
            "sJJcSnkrn+VR+D/i7i+Sp9wtlStSlAEjPLnjlmnBvxtXrO7sVNNIdWLJ6YeUQgNjbSc7fn3rE/FL\n" +
            "bdi6sspQ3ow66hsJnUT50jOfatJxK9f/AMmXbmXfBSZQjGMplPQgkSPfFZOw4Dxq5uitx9bf8Twy\n" +
            "gK0oWI+YEEfTczzoMdcO2qXFLtVXDuo+YPjfqTk1FtbnwFE6SVd+lXvGPh7iDL6isqU0FkBWspSI\n" +
            "OcEz9Jqr/wAluPD8UylgZDysJUO3P96InMXpUEoWkmBjJx29KmvPrdtyElUARI59429KomXSyoBc\n" +
            "57bxVg9ctsam9cSCYjA7+v8AWiHE3RRbhnQopSRJneSBTLgTa6wFfxEJCYHMk7nvFRGrxCnRKiQE\n" +
            "iRyJG0007drdAVhJIAEYyP8AigN19S9hlUSOsdaDYU3aXxXqQo6WxyjJP0xUZSyR4muVZ5dalW/m\n" +
            "sXVPlSmkPIWQf5hCgB7nFFS1vu2PGrdVuopdbcbdCseWANI/vrSuM27HG0u8Z4W2EGB+LsU5LBj5\n" +
            "kcy2Y/8ArMHkag3Vz4/EGniBLiUFQAxIEH8qhMXL1pdB5h1TTiDKVpMEVUWHxGVPXFnej/8AU2bK\n" +
            "lHqpI0K+6aqQIBmferviTovfh3h75QlLjL7zStIhOYWMcsk1SDCcj6VA4g+WTPQTiptjbF94AAkE\n" +
            "56Ac6hspMiTn8q1vDrMs8MD5T5n5KeoSk/qqPpVDK7YtIAdbGgK06YiDE5q9Yf1WaWgClbaz4fMQ\n" +
            "oAkfUfeo4bUsrDpLkAuQcQYwD6k1ouGW6HEoSmAoaRBEyoJgxy3j61q9iW68p/hbTDlsA6x5mrgk\n" +
            "6m5zA/aqRSnUXrjhGdWttwRgH06be1XdwFW6gkBSUFEEKM6c/lTybSzuGw8kaSU+ZMxBmJHvvWau\n" +
            "K5HEropKnipxeokBSidXOJ5RuD61IWhniNqm+bZUq3PlfZP8pBwR+h/Q1YNcE/EhpKF4Dkg9P76V\n" +
            "OYtEcPU6JU22pEnPlPL3qUxVMm7ttBSoOsq/0i4nWCnp1ntWmsuKC2ZlFklKCP5XPyxPsap3r6x4\n" +
            "Zn/MWlFUHSpI0E8pzg96x3xf8TLuUBDJftFwQYkg9wd6CN/iJxS2v7xSFOaUmChJc0AjrChn1Fcy\n" +
            "u0oSf4ekjnC9X6Cp6uLcXYBT+JfLZVOl0akH2VIqFcXy3kw5b2mr/choJP2xQQTAOKLlmjUcyaI8\n" +
            "jQIOJE0CYigY1YFDdVAQzvtStsAUUbmjiaAp2gUBhRoE5oZIoDGBn60XPmaAFGfegKD7UXUUe9Fi\n" +
            "aAonnQiN6VHKk/Y0A+tChBPX2oUElfzyNqUn5YnNH6ZooGrP0FFLQocwcZoLlMEgiRIoJhJkjPKa\n" +
            "dfRBEqKlEAq7dqCVwttBuEqcOkKxJ2A51YeMu4QpYUsNNiEbjUOoqpaXpCiRACafdeLbWhROqJ+u\n" +
            "w+n51ERLn/VVmQMTSWtKnEg4SDJpKv4iTqwKUy2QZmRNUWDqHHpdOkKKpJAAG3apDFsoIWq4SEpc\n" +
            "HhJB+YqOxx0iajN3CUKbTlICtZI3mlP3D1y+la1KMKlMnn1PfvUF8q+Nrw3w7oF1OgBoADUlXNU7\n" +
            "9vrVG3F5cBLbISCsAx3NLubjx0GU8yABkVK4RbvEsKSAS48CBzAG59KCdw8OW1w8y44pCVGUx/NG\n" +
            "d/SttZizb4A09elTaH1ktwNlD+bHKP1rnF8VNXzzZfnQrA3kdq23A+M2I4UOHurbUFaAglcQJ+Ue\n" +
            "s78qVYvW3WzYJRo0v4hY38wgKJPUJjbeKy3GWlMOqtLhGpCVqUH9ZSSmAfMc7YGN60a2nGLK5buy\n" +
            "54QKWmjCQSN4JG39cZrF8VuHLR9SG1L8VtYhl+CI7fX70hW04EtT1gAVg+A2F6EHzJ/oMbdanMXL\n" +
            "txBbWW3AgpSFD5hmD06VhOA8XRbpStL79stKiFN5U2if5gd0x61q2iw05bu21ybgmS4hIhLgUOfX\n" +
            "ngQaLE7wW1D8NeQlDjJJJRgKSNo/fcU9wy7LTDDYbU4lh0I1JUCoak7ED036VUX99brZbcDidKwB\n" +
            "4jJKVJVsCZxEHM+tNcA4m21f3KGVBaNIKFhwBayMEZweuKDaW1lb8Qsri3cWA6s55fU+lZ5jgz/B\n" +
            "r4NOsBzw1SydehSQcQqd9+Qqw4Y5csXIcfbdc15BbGvH8qsHflWnbuuHcZt1vNaSQQhTRIBJHUbj\n" +
            "1qDLPW9uGE3TjZZXGopB1JSDsf6mszxoLvuGz4t4pbCgvWlYASnYkg4mYnI7VueNWCGmDcMNvqSt\n" +
            "OkobSSoCZgjJjvVPbcHNzY+K9ehN3q1NJcGkgSTEmQZyKsRi1sfEpuGgHnkpC9CVKdiCACCMyQQZ\n" +
            "xNUvFru9Dzrj4UtRGgrBxjHTHrXT+JW7FnZaG0PXKGwEeE2FN6TkyokzntIrmPEuLWt074K7fwlA\n" +
            "QdYKYV6iR9qsRRKfJIUtSiuIKj06AUlSkqUpORIkkmTNOPI/iEKQpJSZ+bVjlnmO9QnMKnHrVDqC\n" +
            "lBUUndQSnPKhoEFOohU4NMJWEcxT6HklKkmBI51BKatlOvIRKSFeUlZAEdamXIB4U+20V/h2XEaV\n" +
            "ExKsgqI6dBVW44G1+QwmACRuacF5/wCwctyfKsgwexoG1uKAb1AyjAAxUZclwnkc0vXIAJwOdNLO\n" +
            "ZHOqLMKKvhpxJg6bxJHugz+QquR59+XLrUkEp4MUx89wCD6J/rUZlsqMcyOQqC0smm1FJWrE4jpW\n" +
            "oteKMlxKHFAttthIScYBmMdTWct2li2bKUnVuRzipzFugOalIMmFY3jtVSr78ck3HlSmHM+Ub71L\n" +
            "tuILuglKgUhSZBnYjBH0qDa8KecWMZBGTy6GpNyyuxacQu31fzAIOUK5g9J/ahExT7twQ4palQAQ\n" +
            "D/MBvU+28O30JccHhu5QVGBB3BrO2nGGHGvDfSGwRKVj+XO5A5darLnjPilxnWHZJ0gK2gfy8ie3\n" +
            "MdxU4+q6PZfEtrYqcbQFFYkEbids8gR12qq4/wD4grSVWyWAhaQTBAKSDvg1ym44xc+MEeOVkDeI\n" +
            "/OoVzfv3KiVqk9TyqCw4pxl25dWpKtKVH5JJSPSarE8QvWxDd06kdEuEfrUdalLABO3OmyYONhVD\n" +
            "jtw48sqdcW4o7laiT96b50BkyaBjBz70BKA9SaSswMYNGT3EUhWfSgNO2aG1EIJo99qAE0JMxQ/O\n" +
            "hGaAozQGKEZoh0FAqP7NCKEfWjNFJj7UPSiETNGN6IB60mJwKUdqIb8ooC96FHBoUEvAJ5URPmnS\n" +
            "TRqPSi1GJHtQONkBwKOdJmKXq1qlW5OaYCpzR65B6GgeStMqOkkBQkHpSnXgsxzpgLwR9KGvzaaA\n" +
            "yYOdqlMJ1oIwJxNRgRGQSKn2TC3Ea50gGSYwMdaBLgSGxE5H824pyxHjPjEgdetOXFq4twMp0kgb\n" +
            "jFWdvZ/hrIONNKKdA1LWIBJ/PbagSixSD4MlC1pESNgdye1XHDLVYW34aUrDkpbKfMQlIGcbCoqr\n" +
            "VLzLilrUp9cxjCQI786s7a3cZcKQoKfc1K1IPLqe1TSRScXt21XP4hKACAElO2f7FVUKbcS4gq0y\n" +
            "CP76VoblpT10Wz5FKGoRyx06YqAmyU/cqtivSCsQpO2+BQaPhXE738Mli9SrTrB1OJwqBKtXXG5G\n" +
            "c1F+IGP/AHCbg6XwlYbISrMHIj++VXd1Z+GlPEXypDTC1qlqRqAQCCD6xms9c3Dl9w5SmkBLzLqV\n" +
            "LDA2CpIPeM+lVfiquUl1sBQCdKciCNWdz1P9Ke4PxG4tuIMNBWllKgo6CU6Rzzzo3HJtPDC0kqc8\n" +
            "4VvMb9qabtnGlOpBSHANYG4iN/eTFGWosfiPhlzw+6uLm1dCQosrGsArSfQDP796zNw6bN5a7Nbq\n" +
            "GCPLrgkDng8x9aXaBCbV+3Tp8NnzrBlWvO8dpgUhxQ/FXYulkWxCVHeSdvzn6UVacM+J7y3bVqcW\n" +
            "toCVLRmNxkAZma1HCOK2t0j8T4OoghAXqIB/8lEnG2+fWsBbsXNvcNvMJJbQR4ragY0n8wRPer1j\n" +
            "xuGXijajxbYnUlDTnmBjYjc1FjpTXHrpi9RZ3aUlC0xDqTgnkFDEdKCuEWir1y8tYbU4nxEhIKkk\n" +
            "8/75VhnOKf5qyh1u3bcuG5BU2iFtjHLOrPWpjd1e8LSFpbWtxKgtesaVGd47DnRdXrl4blh8vXDp\n" +
            "nUiEuDSvB3BGB6/asLxnhrf4lCneGOOBKoD7JCgsdVAYGev7VrHONWt+9+EurVhepHifylA5/wBk\n" +
            "Gao3Qt7iDrVm4w40tAC7dQJ8VJ3AkcsbVUrIcUUl2y0NvNp0K8rYX5s8iBiPyqp/BuBnxVJUokT5\n" +
            "BIHrmt1xv4R12zTlsw81dLISoKGptyAchXM7bVkP8vurRPirtioklIAGMbz096IqVoCSc8utFqUj\n" +
            "IinFoPzKBiYmKTM4MTVCCvEGAQaQF96NSdpPtFCPN6VFAK5HajkA/lSZE496AwdsdaCWXy4wxbJ+\n" +
            "VJKieqj/AEAq44TY/iFEFWhI3J5dx1Pas6yTI6zWp4FxpVmPw7oBaWoSceU8pkbURdW3Dym4l9tI\n" +
            "TASpCtld0/nFaVPCGR4TjhLbZBSnQkJNJ4WgqfQ4tPiMEgpbI0hZ7KGxrYM2ZeZK3bXw2iN5BHvH\n" +
            "51FxU2vD20qZSw+4283CTq2j05flTXEUWzNypLiSt4q8jyBGs/7TG+/Or/8AykOsLVbPAwfnkasc\n" +
            "p/5qm4o2VWrhSla1wUkRJHcHl9aaMJfcJF147LaPw90lWpKUmQoxnSeY6jBHSshdJ8BJS6wUuqPm\n" +
            "AT5SBsr+orcqu03XEm9dq42tatLikGFFX/cI3+/Ss9xK1VbXtxb3TyV6PNr0HzA84994HrVRlblC\n" +
            "inWrXq6KM/Q1GUkoUUHcHlnNT3tLYUGyST/LEAe1Vy1AmUiOtARUJiKbCpV9qXGO9IKTM8utAUn+\n" +
            "lAHvmkuEz2oCRFAeZ/vNERnFGc0DigAHpFA7YojyoyepoBvQOBijJojIoCoRQFBJ9KAwZ3kUU75o\n" +
            "8A0WB1oCxRjBmKLc0AaAzkUJG8UDvQPagIHFCkx/c0KCWBgigdiCaM42gZpChJ7daAyQREA0EHtk\n" +
            "/ai7Ee9ACVEcutA4Ug86E+aKISEx0o5HLnQKStIMlM9Adq0NglK7dpBgH5ieUc6zjY1LGCBPWtPY\n" +
            "W5RZ+MVESQCSOXIDtQqZbsLbZeuHUgLTABwYBnl1/WpSOIC5bFsnSLZpSSUJ+UGMkc53plx0mwQn\n" +
            "B1R5tiIMfpTVuhLbim1pLZMjG5M7/p9agtFqtnmQpjU2G0wFKX5lZ5ewqyubsMW6rhIkOKGqJnRs\n" +
            "QPqazRvEpTpDiAlRK1JTkCCTJqwL6neFJfQ0qFNmVKkAHO3Y0qyl8YaNs29dMPJKjhIgpWCeuBEA\n" +
            "8qpXL3S+34DrrroSlRMeWe85inzxp965btkpCUpQDtu5saqH2h+ICkEiV5Ry3x60SugO8SPE/h1d\n" +
            "u3bhIt3EB0OrGRJ1R1HbtWVtrjwro3FtCUOO6tCTI3wPvU1p9pVg1a3N2pAXCS8kyFyISSB/t2ne\n" +
            "ojNs9w51SHU/IZUDjUkbn6UEh9H43iDjiEhTbRKiAmJJgQalfD1r+O4s/wCK94K3UyFuRBSCPv0N\n" +
            "E20lF8pKGFlvSlUSQKeYQ4t9arcBnw2wkqJyJ6xz3oRI4Tw5LfGGr223Q8pISRJUlQkT9Zqn4nbO\n" +
            "rfLTiUONFas6YycY9M+9bLgllbBVsordbUg6fOkpSqRgxNMXfAgoXyfKhMF1BJAhUx9Mk0WxkG7p\n" +
            "1xbFqhWGoSFgZJ9e01Z3y18PaSpTLSyFmHinJSRkKPPlUBhH4N9OlJV4ZmSJj02O5j/ilsXDibxa\n" +
            "FKU2yUgeGCTA/uYqouOG3Clha2bdtx+fKgZIT1Sogme2avOFXdld3KW1F9tlZAW2sgyYySI3qkbD\n" +
            "3DnS4lxLzKsJWEaSDzBIG/rVsy5eXBKn2FOJAnUnQlQHMxtio1Ggv+F27bjT1s0yoEaCsNQpKeic\n" +
            "fpWf4lwS3Vw5tdu8hu5a8qHUka0EdTtHpFTm+IN2dylBdQUkSpIVKkntArQKtLPi1mQ7pfxqCkwk\n" +
            "kHuNx60HM+Jp45ZuMu21wl6ETqLkeIJzKU4UPvWfvONXl234N0wWyTJKQtKVnrnn32rp998Msoa/\n" +
            "C2bmgNqKktOKSdPad9+9UXHPg95ln8S80VEIEMtrVpcVyMkwPQb1dZrnTqH3FKU+klMCDCiV9CNz\n" +
            "UVwqDalIeWmDBS5WnuG1XbAsXEG1WkSFKPhif+4kk9on22mmcZuuHlXjpSpsLKCskrSTHSNjRFKl\n" +
            "DmZTM9M0mSMnFTrywShkXNuqWz8w/wBh6TVdMGDUUUiTG85peI6UaUiN+dJKfMYmBRRgSkZAPerj\n" +
            "hN2lDiG7pAW0D/NuB6n8qp045TUq2eaSSlcgEbkSJ7jpRHUuHpfsktrtFKuLNezZUQUdo51tOHcQ\n" +
            "QhCVqLpBHm1QpPsa5X8LcfXwyW1JcXbrAEap09Inl+VdP4dcMOtAJZeBUmU8yO/9RRV0wuCpVvhC\n" +
            "h8gkJj0IqFfWFrflQfLtusK1FxMjUIyATgjt2pvh62GLgaLvzKOUhRSmf39qs7rhjV2C6zcOBDsA\n" +
            "Kt1SNW0wcUVz7iNsiwuHAtThWkkeOVJCgkjEdQc71l79TSkB51QUsCAEgykdZM4kVseP8G4o45of\n" +
            "QXVISJWFBPiRsZGRjrz6VzPjxet30J8S5SlQJWVIEqWDzz0iqzVZdhLzrmgHxlGSmJJ7bZqrKScC\n" +
            "TUsrQFagpTxG8nTB/OnF3KPw7jiWglx1HhkhMe89xQVp+aaSTintC2mw4QpJUfJI5Dc0wrJBoE+t\n" +
            "AgdKOJ3FAnO2NqAqBkmiyP60DkGgH5UN80DQGTQGZojvRHnQFAPaizmjMRNAc6Af3igZocxQkneg\n" +
            "Ao56UkClAwaAjsOtA7UCYPekz1oFSBiaFEZ7UKJqfog5GTSVJSmcdqdPzGRTaoBJx60aJBCpwJ6G\n" +
            "iA7HpFAAJJIEzyNKSVTCh6AUQCnFGEwZjEYps6lO6UpJJwIq1bt160spaSt9wQAThI/TNAfC7dK3\n" +
            "w+8FpZbBUIHzEfpVtrUtvwC7CCQQJgBW/wC1OXKDw+w0vEOko8ML/wBsn9fsKzzQDtwnxISkHEzy\n" +
            "3qC9XcPJ4e4oJCkqcAQoDfT+VV6XELejxFScwDmCc/SpXFL5pS22WUBtKE6QBvjrVU2pxl1LiCNQ\n" +
            "MpxsaB59Sm7tSmoTqUEAjMD+4q5ZuHmbNTVsorUfnSqCVJxmPSqF4LdeCtyskqgbZqVaXirN4hSA\n" +
            "U6pJUcgdu9BJtW1/5loB1NEkFXNRjf70pbBtWELkKCDISRBg7DvV6htlDX4xlIW03ClITEkHcxyz\n" +
            "vUW4aQ/brCCXG3dTojOgxJH6/WhivunjeNWzqQIZSUqSFRqPX8qtmXFOto8motq0IcUPnTzT+Zqo\n" +
            "slqSw62kJAWMah1qy4Yyy88lLqRpUVEKCiCI55xkcqC5eUG3rd1tYWkBIg4kbR3xUri9o01ci7sy\n" +
            "EhSErW2YJRpOcdY981UNuKC1oSsJcaBU2oxlIO/bFWqrdtLF3atJGUoVqG5VvE/39qL2fRct3jjT\n" +
            "5LjaQj+E0FhQkCSfYffbara9uCi4Q828PCctghGkbqGIg75JmsWw4u0efa0pWEn+XdQKsx33+9ai\n" +
            "2W3d8OtyD4g0rcRoSPLMzneRgdJiiyoHFVtEeIspCho1qaQEgGPvk1W8WQluxtHGC5L4hSSIgzvk\n" +
            "/vVo3aKevmGXmjKmpDIyRAPmJ5kmBUridupv4eXCSVsuykESUBQn9dvzonbPfDqbxL5Q26vwlQkp\n" +
            "KioZ6nO36irRHEfwXEEpdOsJT5m5nM5k7cxVQi5Tw7jBQLkBonSSkYjeMd5HvS1FLi7lS0DK51gQ\n" +
            "rIGPWaG8Nwwqzea8ZlsKUhUaTqED2/XFWHDSw+44ENqtn0jU3A8yvQHcHasUy3dW7HjLUlxLUhIV\n" +
            "vAjOM853q/4LfKgvrJcda+cKynzcs5FFXT/i3oX4qD4aVEnUAFjpSGOL6Su3uBbqAEDxUkFQPaP3\n" +
            "p/iTqbi4QWm1hp1IUSTORHl/p61leNsOoW3fsPqWAFBrRBUlUxgHkJ/KoJ9zwvh922668y3DhKA4\n" +
            "pJ8pBwMcpODWT498PXBZDVofKmUOKLpXKeg1ZFE5xS+afSlKlFMQpElKojod6t7P4g8VoJfMIWCn\n" +
            "UmJ0nrGPYiiaxdrwx20tn23G3klwQfHwmOcRVRd8NbCFLZTCJMpVMgevMd4rob/DmOKJU/avu/iI\n" +
            "wAJHby7EdjWTf4Uu3JQrwxcpMFlSNOs9QJj8qqVlFtKa/lVHI7/em5zJ+tXl0hrwFsu2fgOtq3BK\n" +
            "VH/ySZ+1UxAClBSAPtQJ1CenpRgajIEzSdPMQU0tCSMDb1oLvg762XUqRbB8bLb2Ch09a6Dwu9WW\n" +
            "m3uGeMq3GFsE+Zk/9qtx6VzC1cfZWFNgLA3APLvWu4NxVt1YcbQtFykQrw1DboQdx96pro6b78Xb\n" +
            "6HFtuFQwSIWM8+hqY1dNNpShLy9IgKClwUxzEDf2rOW1wL5j8PqSqBulJC/XPOpXh3DTyV2zyyQA\n" +
            "nR4cKV1B2morQJuWbxYUXCHBGXAdJBwCD36Gsf8AF3BW3eD3X4xtIUklwtpkaCOnUxVwhy5U8llc\n" +
            "pC8raKtIXvy5kdKj3jr99ciyuX3VW8AwkQB3gjP9mmjhz3D3mntKUK0KIgnlPU7UVwlxqWlJBCBr\n" +
            "K1ZEcq39++1aPP8ADb9w+C4FBOpMJifmkCQZE9KyXGuGNcOAQi7cuLVelSXENiFGMDVOY9KqKJ11\n" +
            "b69S1KURgSZgUyc7iTT6gkKHlKUE9ZJ96SstR5Ur/wDsZoG98UR7UCZ2FFkDMTRQIjHOiNHNF3og\n" +
            "E5oAwKI0JoCmTR8qKhQGDAoUJmizQChykUKFABtzox8tEKAoCiaGxo+XeiJoAZnNChvQpotHhpcI\n" +
            "3zTSjnbJqReDRfPJ2KVkb96jnJGaAGE7jlSZJVg4PI0SxPtypCJCtvvRU23QNXlAMZn0qdbLOoqk\n" +
            "BSj5gnM/3yqqDpaHKe9S2nwhrWQJMpGdsb0RY35cebQkqIDpIVJn3P2qtUFlwJwCrBnJSBUpK23b\n" +
            "UJLm6Z0qPPt/fOopaOlRbTKSQD5hv2qCQWQXlLJK5iFHnPWlsslLqFKAVMwNweVBtelgIgBA8s9a\n" +
            "nWjRVbF0AEtqOn17/YUDbtu2hkohOrTPlH0+m9QyyVohQ/7yevery1s/EWlxZhpIhUiI5fWaYNk4\n" +
            "207LaQYkkZGmeVAhi8Vw20aa0geKJUlX8yYyCOlTLfVbpDzAbFuswptSiSCe/KKoXlKvHi7JSlOA\n" +
            "ZzVvwq48NgtOrWZPkCzhJzAP99KBziDbCQFM51kLwckRnsP1q34RYqe8JSSkFJMHVGD/AFH3qgPi\n" +
            "M+GV+RtQOlUSD71pvhm50vNJU026lQ0BR/lzifvmizs7xvh7Fs60tsNspXhtIlQkySJ2nbFWdlou\n" +
            "+FhxMNOAhE6omAcjEqORikcS4Tc3njusuHLnito1iSASMj+9qr27i7bC0rBYbZwSCEkkiD77RFRV\n" +
            "bxl5FlxdxlMqtlkrXKYOBuPzq44dxEWyG2VLhCU6GyNgkkHI+uT2rMcVuW32UIGr8Ro0OlcDKcAz\n" +
            "+dDg3Efww0rQpwAlKwMAyPSSNulVn/Gy4ot9i6FwhRKURrbSrKTMg9xt9auRcB1m4s3VoBeJBTA1\n" +
            "rByCD6xWVt75uC7bpGko0wpRlQHKTzEfSpz77LRt31a0ltBbLrRJKCDKftUaUvxNbrtrZC026EBt\n" +
            "6FY8ygcifvS+Gaby0uVNj+IloShZjzYEjtiPep11dp4rZvNXiUh4eSXZAUDhPvMc6qX0PcMZaRdp\n" +
            "U2hgaUQIUSoTvzyKJe9XLl48zYpc8viR8sCIAGCO4j6VYcDfZvFJU1/CSCZJ5p3Ej1rPWPEUP2Dj\n" +
            "brjj7CioLKUjU0nkrvk7bxRWHiW7FwzBLmpIASYwThWeWMetCV0LiaG1M2wIAbQNZW2YhQFVVxxF\n" +
            "LAaXdtfw0L1amx4gSdiDPLO4qZwW+bvbIMOiCDoUgnc9+majX/D1hH4228MqSYU0ryiPfnv3oquv\n" +
            "m+G31mh63t0uuIPNZn6dKo0t3Vo+oI4eUgTsJknAweYqweKW3lIZDjLrUrCNYIkjr0pCk2/xLbL1\n" +
            "pbbuGkSXEuaSesftVSovCA6146mllvw8pDiSNM7g9M8uVWbjVrx55q0uFBLyRIcSdMnf3B/Osbcs\n" +
            "X9hci3Tcy4v5VNlQCgOhMT6U41cPXLCGXUnAIKxnSdpMdOveiaVxfhymQfFb0HWdLiVeYQdlDkfa\n" +
            "su+Epd0qKlJ5Ty/atNfLRecNbuLoOF9ohpT7ayQUjAJxE8pmqa/tbq0ZbcX4nhrEtKUmUqHqOfrQ\n" +
            "VMhBhJgdIxSkqkyDtkwKbJKVQsUElIX1xVFpajxCAhSJOIOM1NLbOsB1BbeGz7KpH/2T+tVtuU+U\n" +
            "oUQSIKSn5j0qwY8y0tlsKEzoWrMetQaGwVxBhKFLQ24MBLjeSR6iP75VqrfiDi2Uqurd0pQqC4FL\n" +
            "XHbG1Yy3avLZoO2anEJmf9wHQac/lUyz+IL6xfUl9p4JOCls6V55gUI2zHEUMNha9f4ciE65USTO\n" +
            "w54qRZIdUEXCwlSANSAG9JA6ZzPp0rOscVZ4myBbw682uUpcbCVdJBBwe4FPJvXEM6re5LiwrS5M\n" +
            "kA7wSPzP1ovCVx3h9vfutL8vgKbUVsLQSlOMxOx+lZHjVjw60sWrJDqi2QojxCVJycJ/7SO/WtN/\n" +
            "mevxGbxopKkkIU0oEA9QRuOxFZG8ReC0vBrQZGkrQmZ6GCMGOYolZu44O2p7U3eeNGAFwggDlk1V\n" +
            "XIcSCkNFloTp1JgkfrTq3XWFlLqg0NtKACuOufzphV2pTakAKImfOrV9qoinM0VOKWVJgpQO4TBp\n" +
            "BgbUBTg0DnbFJxNHNAfaiOwoum8Ur1oExQ/OgaFADtRbijmizQHtQoRzoGgAigM0KAPWgMwIpE5i\n" +
            "lTSekYoB9KFCDyoUF3xkeFxq9b3KXljHrUIETMyatPihks/E3EUE5S+r86qU5VAG52pA422t5elI\n" +
            "B5knYDrSD5VeXKevWluL0IU0hWDGsj+aOXpTSTKiaBZ0wJE+lOeJ5Ep0jGZOaa1ApxP0o21mdhtu\n" +
            "aB1SlGNPlTv0p1CQoAqUZJiKa8TWmCIOJPaltqAEY6mgnoSp0C2CobnUTGAOp6Vb2TTmgrabCUAE\n" +
            "QSYV2jvVPZuJcWpR8sqHsKtmuLAtpBEtNglOPMEnHXeagtS14DIS8khGTBMdzJ96gcRNwuwW6EOK\n" +
            "aWZSmCkGeeckACrviF/bMfhXSVKdUwkKUoAhJMbH22rMcbuHH79aEuE27Ti8p/mzufX9qLeOFcQG\n" +
            "RoKYIMnEz2mpNuoPsEpe0wJ0gbfWopSkAIROlxIJzt2ovFJV4ScKOIAiiLkOpdYbYuNIQQAhxInT\n" +
            "0xzqbw+9Vw13QGULUokeMTAEjJFUBaWrSGFKUDBIjMc8f3ipa0PtlKHIKQ3pCVbtj96DdcM4o5fW\n" +
            "rqQhtItsJdcAgD/t7859apLhhKi6Q6XEBWkPH5V74AG+c1k2751i4UVIQoIUdIUPKPQfp3qwb+I7\n" +
            "hLZSplBQpUmBEiPz6Gpi7+o12oPvg6gJTG2BmKDIKb1M6dAOkAH9aYeulXBLzaVBQSSQTOx5TUmw\n" +
            "cRc2ZSpADiQcE7j061USy8bO6PjulLUhRjBVIkEf3yqezeqSlaHgPIzqwvkjE1mblTvhjxFqVp+U\n" +
            "E8qmcMlxRlwypBQSRqITsfpg0GxVe2FxbXQcSG0N/wASVDCo3mNpqhvH32mx4Tri0Oka0OHxARJI\n" +
            "n7U1YXzAuVtqcAYLajKh83lIj13p3hFw2/Yu2ziUE7ecydoEd5+1Dsli8dbCizbWzbnRlEYOIOfU\n" +
            "Utm9Z/yp5p7xEPMLgIBBJE7Tygk/Wpb3C22eFtuLcQorHnCV5GQBHcTPaqTiLKre8UElJVrJ0kYV\n" +
            "AjPXnQa7gPF2W31JW0lpK1tglJJClEHfO+J71qrHiAeedtfLCgU6VidDg5j1E1yawu1stqtU5aC0\n" +
            "rJiII2Mx3rZ8JvA4nUoaQY1OdM9dvr9qLKvULb4kw6lhxeptIKUrHkcSMxnb+8VSucNS82tX4RaH\n" +
            "GiorbKQVDOQIgq/pU25W9bJQq2daeYGCUkykE52mPWqV/i3EUvqWhaX0zLjekBQERBI7YqFxW8Ud\n" +
            "ZW0lGpx61EhLYQUxIGUnP0+1Ur4eWA5qacaTukL0wOmknP3rQuPfiGktJDiUmFKZWjKSB82d+dUt\n" +
            "wllq7R5rWG5lREON9ykziriUq3uBZlxDNtobcgrGsFJ6AjJntSkuqLKjbpS0lQhTQnQrrAOKguXI\n" +
            "OtENPIUPIpJiIO45e1HarWokAtzpgQoebse9EQbm1U68tQjUBKkjED0qEW4WBKSOoG9XbbaHw0HR\n" +
            "5p0TGUqzg9J+lRL21Sy8C2qUEYB3B70imW28pSDCj/uwPrUxovlwebWQNlGSR686hysJJKQpJxKv\n" +
            "3qwtW2Swda1g4hJGAfWaCYw+UOJUtS2l7hSXIjtFWqUm4OlzxFFIyUPJUr1TJk1SpbFushxa1o6h\n" +
            "O9O+C2k+I042UgwACUqB6kVUaFDiy2RodKUiWyGgNUf7gDv3qeltDjKrkvXFq7A1JeCtK+ysbdxV\n" +
            "BbcVt06EXAJzjQhKiO6ucVbM8YttCQhhK9IkBRPXp0osxEd4UbgF23QdZEpLTmF9hnP94pmG3LZx\n" +
            "tFwrI84nJMcxPmHoKt13qAhPlKFE6my0T5PYxNZ7iBQm7IacaeE7rEkfbH3qCruuFpVZFSGWwZIS\n" +
            "FrwQNyk/pVM/ZMtpQnxUtqiVKcBAHYczWjuW71CEeFCRsAkb88EH9KpLptxKwLpQSXBI8RGkkdQR\n" +
            "k1RVOpbQpSUr8RMYUBAP1zTOafdQorVkqjEzM0yQcyKKTQoHaRRcqIFCRsKFA7TQCeUUI70VCgOh\n" +
            "NFQNAYMZ50VHRA/SgHKj96B70NjQEfSk0rY0kmgFCi3oUGv+OlafjXigEQHox6VnAcE7ntWj+OxH\n" +
            "xvxUAf8AzGcdhWcO9SdLRjO1KPOk7SRvRazOxqgRkAGjnz5otRPaiSTzj3oFFR5UtKiSOlN7mPvS\n" +
            "wCBt96CQHAlpSRGpW2dqdad8NooWfLgwDyFRBjmIpbSgFSQSBn1ojQXFwLq3YcVqlA8IpJ3IG9Fx\n" +
            "ZEuo0o0JdTrPcQP1oiwkWzi/KE6goavNumZ/T3quefcuFtr1KV5dI9qgNSPLpQkasFSuQ7Uh5Wlw\n" +
            "LwCpIKiNwKQCpLayrUeuce9LccLgSoEhUBJxyoLi2eYYsC+pWZOlMZOOXtTDTqdDy3dSgoQESYIn\n" +
            "FQHCUlKRlAMAHMGntZaQGVpIwFIMTjNA3fIS5cpKIkpyANiaircU3hxYOOW1PtOaXitSgQU5Ec6R\n" +
            "cpHiFoRGNJPpvVCGlgpEHAO3WrC3C2ll9pCgEEjzKyeu+9VASEqML23xT7L+grCVgpIiCJqCQtan\n" +
            "nVlwwDkdyaZt31MvA5wZjpS7idkYCjMTAEU03DjgMknn1pBPbtkusgIlPhplU5GTinrVsrR/DUQt\n" +
            "YJhOSAOQ9RSLQtrbKC5CYlWhPTABA69aYsnEPrS022pBQvXrJ2gbYoNMVLu7Nx1SkeMkaE6FaRBT\n" +
            "BTA69etUV4/reDjbaUlPlgnKQNifWp/DbxIS5rt++sbYiJquuwfxK3FJ1hw7gRqnr+XtQR7R1xLh\n" +
            "S25J0kyTuOlW/D7l7SFNrcadA0QCQojqAaprhbbE+GoOkkHmCg9CD/WprDrrtyy6tLbenGpCZgj2\n" +
            "3oLax4uWbu4tXlEZ1JlMA+wqwuX2nGULICmysyWlQVncTzx0O1ULlqq6uU3KlqKFT/E/2/t+VNpu\n" +
            "HrVKkIYQVtKnXHmT6xhWKCVxZx4arm3eC9IhRMxPI9BOJqibunEqFwslwAyQTMHsdwa0qeKM36kW\n" +
            "t2wnwinWkISUkxuCeeciqJ61L10p1lCltBJVBP8ALnPr1oDYHilK21OqWk/xGlgGB+v6UV6H7dYD\n" +
            "iFpIymUEFPvzFOsoFk0lbLjgSST4riYk9MyBH604p9hA1Oh1byU+cgkg+4igYRxFagHHfDW3hCgl\n" +
            "OSPX9ac4o5pvCGyFNODWyeRBHymlN3fCi0UOIdSpUFUpkj0M08xa2t+Vot7uHW0hxCFpiNPMHnjl\n" +
            "zoKZT7ihvEjkcRTrL60SlO55p51O41wxdoG7oApDo/iDkT1FV7KEuRAIPUzg9aosmbxxrylCtZxB\n" +
            "UQc9RzqaLVL2la2yXM9x6yP2qKwxdlsKAcWACQY1AjvUqz8fWFttL/8AoklP70RLNohSdLspUjzI\n" +
            "PlOPUxPpNBoMIuA0t5KivOlgxp/MD0mn1pWpstnyAxqbUgA+xIMd9qZtmFKfW0ULt1IOoLQJgdCI\n" +
            "/I0ARci3uHGXkBxkJg60EFSTsT0o5t2n9dqA4SkgBr5kHoZwsdjmp7an23Sl1DTgTMBKwCRzERiq\n" +
            "riVqm5uQbJtxpZwErSNM9jjPbNF6PvXjbzJItUtNq/hrcWqCknYmeXtjrWLv0+DcLRCwBIKVmc9j\n" +
            "V9eN3Fr5Bb+OVpCtQUFQO+PzrPXEqWZbUEzkKTOn0oIw0xOohXTJpsmJAJI9KNadJmDAxkU2oj+W\n" +
            "aAiIO9Ed6M5pOeVAKOgB2zRGgGd6EjnQobZNAM0XOjmhzxQHmiGDRxQjNAUzQ70IoRFAKRzpRJpP\n" +
            "OgFChFCg2fx9A+OOKZB/ijb0FZdWZwa1f+IaPC+OOJER5lJV/wD6ismSTUnSjExEH3oKmYjNGkYO\n" +
            "KMjUaqEmd6LUO3tQVIxNEZiJHrQKStONqWokiRgTTCU+bnTokDGRQDXn7U42qcASZ5U0R1AJp628\n" +
            "h8TUITkCck0E+8vSLcW6TKUASQMmoLbnhg48yh9BRu/xHlKMJTuQBgdBSIU6ZODIMk0DyFK0KJIg\n" +
            "ECO9TOHJC7r5S6QklWo7wCZHpio7DcrDYTJUSCe3b1qVZoKiHA0EHTpCTgY/eKBkqQqHSRqBzBnf\n" +
            "tQduVFg+VR1zGrYcqW81rbWswlTh1EbCkugoQlsRjIT3qCKH3BoSAAEmVd6IuhTOoxKYGqnVpcUg\n" +
            "rSj+UjypzP50htsJQVnQqYxyFUJc1L0q07jkKQEDUZBGOVSlWY8bw1LIKcTOD3pDiSh1aEnUkZJP\n" +
            "PFASG1uNCACpJ/3b0w2CFDUSEjmM1ItSD/DDalKUNpikpb0qIWoAdEmce1A5aLdVdI0rUFE5SnpT\n" +
            "n4k29w4lBcVrEaRgDrTFs6WdSmkw5ISkEyZ7UttGo/xVIClKznPt0qCw4cbhZ1pbJCTOoyIqz4g4\n" +
            "3bNoD7bqnAknxQCADyiqy2ukFOoMgsoMQPtU1zxOI2iWFLQQiFA6hAGTg8t80GdcLiypC24cSJkT\n" +
            "Pp6U9ZKhXhJJBX3OO+KsHUsPXXhot1rDY+TMHv1J+1RbJxSilsDw0ZBUP5pmAaC7tHZt22F3EM6t\n" +
            "Kxp/0z19J59KjvkFh9lkJdBO6Mk5iBOZJnNL4cXFKda1KBByHCYSSQAn1j71Ouj4a1NKdgXCSnJA\n" +
            "Mnc/n96DO+CGbNbn4htSgsJhJ/m/YARinrl78K27bt3CVt6ilJORyJI+30qwuLG1sWbhBaLklIQo\n" +
            "SIjEkHY5qHY2zDqlOKbSGxKUAJmMGDnHI0DdmlDNw246UONOD+JCgDP5TSLpLa1OpT/pj5XAdhyz\n" +
            "+lOu277vDHEuOEBvzJGkRuO3TNVbVypltlTSylWs6ecgcyOs0BradQkJcQpQPynNT+EuO2zwX4Rd\n" +
            "RBSoZ2IgipSb5DzX8dptzUIypUmO4OKaKLNbSvIpknCQl0xPfFA/aXjNwyrhy9LLKlZ1kkJPIg7j\n" +
            "7ihc8MfswUKAeYGziVYT+hqpdt9C5SVJjcaqu7dZPDm3kg60KAiJSsdCOf6UDSUJU2Am4QpJOUiA\n" +
            "Y9sGrywbSxoCHmsgkh1MjOMxms6bYm6SVDWFJ1AJO3brir6yBbbbS82VJJkKJ+X0qjWpt0qdbQm6\n" +
            "WhZHymFpOOYPLtVPdMW7SlMlh5KtRksmRynMbVc2NvbIQ2ty3U2pz5XErgTt/Yo7u3Ot9AWVISYK\n" +
            "goAes7zRrtk/AZYt3HC46psCQotQr/7EGfeIxTVveWiXg8h5K5ElL6/n7b1ZcVFgmzDbrrkR5V6w\n" +
            "T9xWM4lc2iFJSgKc/wC8xJ+lGUji1w4pWlRcBSNORJIJmCchUcjVSp5JbCUpmBkaiM9qiu3C1ulR\n" +
            "M4x6U0pyBjnzoEOEaiAVRPMzSBg0fzEyaInNAD60XKjnAM0kmcmgHLvQobY7UNjAoBRUrnRT+VAD\n" +
            "tQ/Sio6A6GJI50OW1DegLmaMnFDNEc0BGk0dEaAChQFCg3P+JqT/AOtLxQM4RJ5A6RisgkwJPOtZ\n" +
            "/iJ//WV//wCQ/IVmEbmpOlpIUMiKEwNvrUr/APRo/wDI05Y/9c36GlpFcdRO4o+QkCa1Vnsa0dj/\n" +
            "ANM7/wCA/Sp7GOZgHPX0oxPUeldHf+Qf/kFRR/8AzA+v61dGCSy66qGkLWqflSkk1Z2fw/xm9QXG\n" +
            "OHPlpKoUoogD612D4a+ceiq1lt/rj0P51L5Ljzuvg/FX302zXDLvUlOrT4ZJV3qS18J8e1pP+VPk\n" +
            "RIJAH516I/8A7nqfzpbe5/8AKppjgJ+D+PNIZd/y4uLUdOjfT3OcVZM/BXGUBKnGVawYCUkHy5ki\n" +
            "D7e9d0PzD1NS07VL5VfV58d+C+Ki6Qk2bzqVGVAbp6Ccgf0o3f8AD/jn4l1SbZbqB/pq5kbkkHY1\n" +
            "6GRt/wDb9KCtj6irqWcvP6vgHjLiHQpGgKQAApKhBO8Y+tUz3wTx9lpITYrWCpSiQdgMDB+telXf\n" +
            "l+lOL/06k8rhkeZ+JfDfE7dwr/CXBUEAkNAqEYA29CahN8J4sdBVwm5W4sGD4ZGa9Ojc/wDhTLvz\n" +
            "H/x/Stex6vMn/pnj7q4HDbiSMADl2qQ38FfEKk6k8OcPLTqE/Sa9IK/0z6ftTLf/AEyfemmOAMfB\n" +
            "PG2ULKrNYUccj7T/AH0oH4N41Cnn7dSSADoCSSsn+VIA/pXfk/J9aSj/AKj2pKerilt8McRZYYcF\n" +
            "g+4pS8pKSB6wOVH/AOkuNu3w8Kx8BKQdRwlMEnaJruR/1kehp/8AkT700njK4W58E8b0ymyUVLSQ\n" +
            "VgGB078h9aip+BuPtNr8PhjrxSqZQnO2ckjPKu/N7n1NBf8Arj0qaergFn8KfF2ty4Xwa6l15BIK\n" +
            "R5UiT15GKtbT4O+JVOhTzTjTgUo+KpUz02z+0124bJ9ac/l96l8rFni42eA8Zd8Zi44atSCsJS4h\n" +
            "IyBzz33NRrT4Z40m6ubb/K/BtXNSi4lISCQDp2z9OtduG3vRDf2NXbYlmOA8S+Evip1oJTYAoWgp\n" +
            "CEQCkQN/pVP/APw6+KVpZbRwtyUzB1Jjr1r0ivcetGj5x6mmpjz1a/AnxSxoI4M8tQJBmI/MTVux\n" +
            "8EcfbQop4WpCv9pUI+lduV81E5/q+xq6vrHFmf8ADrjrqVFdvbIXMglwBX0ipTn+H3Gg0EIaYBa8\n" +
            "yIUnJrsKflRTatj6fvTUxyAfA/GbhsNPWqA6lRIdS8ACDyCTsKmM/APG2Z8PRE4T4oH6RXRmf9Vz\n" +
            "2qya/wBNqpo5rafDPHrEgnhzCjPztXQBPqIzTHEPhvi7iZ/y113YqRCRnnBCv0rqSdk0g7j1q7wZ\n" +
            "y4fe/CHGGNTn+UXCte+Av8zNZm7+HuJJ1FHC3tX/AOFYzzzXpD/5zRp/m9f3pKY8sucB4sZH+W3J\n" +
            "O3+maB+FuPnbgt/7MKP6V6Y4l/1Lf/gPzqyt/wDp0etLSTXlVXwr8QtolfA+IgTE/hlftUZXA+Lo\n" +
            "Pm4XeiN5t1j9K9gHc/8AjTK6urjyCeEcUCSTw68gc/AX+1I/y6+yfwdxjf8AhK/avXr3+mr1/So7\n" +
            "PyL/AL5U1HkY2r6BKmHBPVJFAW7xOGln0Sa9Q3e59FVDGzHt+dZ9keazbPJHmaWPVBpKbZ5Y8jTi\n" +
            "vRBNerLr/SHpRWX+oPetaPKPhqCspI9RRQQYr0JxD/pnPf8AM1y53/Tuv/Bf60lVi5ijmrviH/SI\n" +
            "/wDEfkao/wCWrECYNHOKUvZNJV+tAg70VLO5oudAaUgjcUKsrH/p/c0KNTxf/9l=";


    /**
     * 从指定freemarker模板所在文件夹
     *
     * “/”            对应 classpath
     * “/templates/”  对应 classpath下的templates/
     */
    private static final String DEFAULT_POSITION = "/";

    static {
        configuration.setDefaultEncoding(ENCODING);
    }

    /**
     * 导出excel
     *
     * @param templateFileName
     *         模板文件名(含后缀，如:abc.ftl)
     * @param resultFileAllPathName
     *         结果文件全路径文件名 (如: C:/Users/result.doc  再如: C:/Users/result.docx)
     * @param dataObject
     *         与模板中的占位符 对应的 数据信息(一般为:一个专门创建的对象， 或者是Map)
     * @return 生成的word文件
     * @throws IOException
     * @throws TemplateException
     * @date 2018/11/16 10:52
     */
    public static File doExport(String templateFileName, String resultFileAllPathName, Object dataObject)
            throws IOException, TemplateException {
        return doExport(templateFileName, DEFAULT_POSITION, resultFileAllPathName, dataObject);
    }

    /**
     * 导出excel
     *
     * @param templateFileName
     *         模板文件名(含后缀，如:abc.ftl)
     * @param templateFileDir
     *         模板文件所在位置名(如: "/" 代表 classpath)
     * @param resultFileAllPathName
     *         结果文件全路径文件名 (如: C:/Users/result.doc  再如: C:/Users/result.docx)
     * @param dataObject
     *         与模板中的占位符 对应的 数据信息(一般为:一个专门创建的对象， 或者是Map)
     * @return 生成的word文件
     * @throws IOException
     * @throws TemplateException
     * @date 2018/11/16 10:52
     */
    public static File doExport(String templateFileName, String templateFileDir,
                                String resultFileAllPathName, Object dataObject)
            throws IOException, TemplateException {

        // 指定模板文件所在  位置
        configuration.setClassForTemplateLoading(FreemarkeExportrWordUtil.class, templateFileDir);

        // 根据模板文件、编码;获取Template实例
        Template template = configuration.getTemplate(templateFileName, ENCODING);
        File resultFile = new File(resultFileAllPathName);
        // 判断要生成的word文件所在目录是否存在,不存在则创建
        if (!resultFile.getParentFile().exists()) {
            boolean result = resultFile.getParentFile().mkdirs();
        }
        // 写出文件
        try (OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(resultFile));
             Writer writer = new BufferedWriter(osw, BUFFER_SIZE)) {
            template.process(dataObject, writer);
        }
        return resultFile;
    }

    /**
     * 获取图片对应的base64码
     *
     * @param imgFile
     *         图片
     * @return 图片对应的base64码
     * @throws IOException
     * @date 2018/11/16 17:05
     */
    public static String getImageBase64String(File imgFile) throws IOException {
        InputStream inputStream = new FileInputStream(imgFile);
        byte[] data = new byte[inputStream.available()];
        int totalNumberBytes = inputStream.read(data);
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }

    public static void main(String args[])
    {
        File resultFile = new File("./src/main/resources/files/222/333/4zzz");
        // 判断要生成的word文件所在目录是否存在,不存在则创建
        if (!resultFile.getParentFile().exists()) {
            boolean result = resultFile.getParentFile().mkdirs();
            System.out.print(result);
        }
    }


}
