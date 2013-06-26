#ifndef _PYDATA_H_
#define _PYDATA_H_

/*
a a ai an ang ao
b ba bai ban bang bao bei ben beng bi bian biao bie bin bing bo bu
c ca cai can cang cao ce cen ceng cha chai chan chang chao che chen cheng chi chong chou chu chuai chuan
chuang chui chun chuo ci cong cou cu cuan cui cun cuo
d da dai dan dang dao de deng di dian diao die ding diu dong dou du
duan dui dun duo
e e en er
f fa fan fang fei fen feng fo fou fu
g ga gai gan gang gao ge gei gen geng gong gou gu gua guai guan
guang gui gun guo
h ha hai han hang hao he hei hen heng hong hou hu hua huai
huan huang hui hun huo
j ji jia jian jiang jiao jie
jin jing jiong jiu ju juan jue jun
k ka kai kan kang kao ke ke ken keng kong kou ku kua kuai kuan kuang kui kun kuo
l la lai lan lang lao le lei leng li lia lian liang liao lie lin
ling liu lo long lou lu luan lue lun luo lv
m ma mai man mang mao me mei men meng mi mian miao mie min ming miu mo mou mu
n na nai nan nang nao ne nei nen neng ni nian niang niao nie nin ning niu nong nu nuan nue
nuo nv
o o ou
p pa pai pan pang pao pei pen peng pi pian pian piao pie pin ping
ping po pou pu
q qi qia qian qiang qiao qie qin qing qiong qiu qu
quan que qun
r ran rang rao re ren reng ri rong rou ru ruan rui run ruo
s sa sai san sang sao se sen seng sha shai shan shang shao she shen sheng shi shou shu shua shuai shuan shuang
shui shun shuo si song sou su suan sui sun suo
t ta tai tan tang tao te teng ti tian tiao tie ting tong tun
w wan wu
x xi xian xiang xing xiu xun
y yi yue
z zhai zhang zhao zhu zhuan zhuang
*/
const size_t data_v1_size = 425;

const char* data_v1[] = {
	"a", "ai", "an", "ang", "ao",
	"b", "ba", "bai", "ban", "bang", "bao", "bei", "ben", "beng", "bi", "bian", "biao", "bie", "bin", "bing", "bo", "bu",
	"c", "ca", "cai", "can", "cang", "cao", "ce", "cen", "ceng", "cha", "chai", "chan", "chang", "chao", "che", "chen", "cheng",
	"chi", "chong", "chou", "chu", "chuai", "chuan", "chuang", "chui", "chun", "chuo", "ci", "cong", "cou", "cu", "cuan", "cui",
	"cun", "cuo",
	"d", "da", "dai", "dan", "dang", "dao", "de", "dei", "deng", "di", "dia", "dian", "diao", "die", "ding", "diu", "dong", "dou",
	"du", "duan", "dui", "dun", "duo",
	"e", "ei", "en", "er",
	"f", "fa", "fan", "fang", "fei", "fen", "feng", "fo", "fou", "fu",
	"g", "ga", "gai", "gan", "gang", "gao", "ge", "gei", "gen", "geng", "gong", "gou", "gu", "gua", "guai", "guan", "guang", "gui",
	"gun", "guo",
	"h", "ha", "hai", "han", "hang", "hao", "he", "hei", "hen", "heng", "hong", "hou", "hu", "hua", "huai", "huan", "huang", "hui",
	"hun", "huo",
	"j", "ji", "jia", "jian", "jiang", "jiao", "jie", "jin", "jing", "jiong", "jiu", "ju", "juan", "jue", "jun",
	"k", "ka", "kai", "kan", "kang", "kao", "ke", "ken", "keng", "kong", "kou", "ku", "kua", "kuai", "kuan", "kuang", "kui", "kun", "kuo",
	"l", "la", "lai", "lan", "lang", "lao", "le", "lei", "leng", "li", "lia", "lian", "liang", "liao", "lie", "lin", "ling", "liu", "lo",
	"long", "lou", "lu", "lv", "lve", "luan", "lun", "luo",
	"m", "ma", "mai", "man", "mang", "mao", "me", "mei", "men", "meng", "mi", "mian", "miao", "mie", "min", "ming", "miu", "mo", "mou", "mu",
	"n", "na", "nai", "nan", "nang", "nao", "ne", "nei", "nen", "neng", "ng", "ni", "nian", "niang", "niao", "nie", "nin", "ning", "niu",
	"nong", "nou", "nu", "nv", "nve", "nuan", "nuo",
	"o", "ou",
	"p", "pa", "pai", "pan", "pang", "pao", "pei", "pen", "peng", "pi", "pian", "piao", "pie", "pin", "ping", "po", "pou", "pu",
	"q", "qi", "qia", "qian", "qiang", "qiao", "qie", "qin", "qing", "qiong", "qiu", "qu", "quan", "que", "qui", "qun",
	"r", "ran", "rang", "rao", "re", "ren", "reng", "ri", "rong", "rou", "ru", "ruan", "rui", "run", "ruo",
	"s", "sa", "sai", "san", "sang", "sao", "se", "sen", "seng", "sha", "shai", "shan", "shang", "shao", "she", "shei", "shen", "sheng",
	"shi", "shou", "shu", "shua", "shuai", "shuan", "shuang", "shui", "shun", "shuo", "si", "song", "sou", "su", "suan", "sui", "sun", "suo",
	"t", "ta", "tai", "tan", "tang", "tao", "te", "teng", "ti", "tian", "tiao", "tie", "ting", "tong", "tou", "tu", "tuan", "tui", "tun", "tuo",
	"w", "wa", "wai", "wan", "wang", "wei", "wen", "weng", "wo", "wu",
	"x", "xi", "xia", "xian", "xiang", "xiao", "xie", "xin", "xing", "xiong", "xiu", "xu", "xuan", "xue", "xun",
	"y", "ya", "yan", "yang", "yao", "ye", "yi", "yin", "ying", "yo", "yong", "you", "yu", "yuan", "yue", "yun",
	"z", "za", "zai", "zan", "zang", "zao", "ze", "zei", "zen", "zeng", "zha", "zhai", "zhan", "zhang", "zhao", "zhe", "zhen", "zheng",
	"zhi", "zhong", "zhou", "zhu", "zhua", "zhuai", "zhuan", "zhuang", "zhui", "zhun", "zhuo", "zi", "zong", "zou", "zu", "zuan", "zui", "zun", "zuo" };

#endif
