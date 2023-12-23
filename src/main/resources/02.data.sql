INSERT INTO users (uuid, name, nickname, password, email, bio, avatar, level, deleted, deleted_at, created_at, updated_at) VALUES ( 0xB3C410ADCE8F4B239127301AD5F60E76, 'chilfish', 'Chilfish', '123456', 'me@chilfish.top', 'hello', '/files/avatars/user_b3c410ad-ce8f-4b23-9127-301ad5f60e76.png', 'user', 0, null, '2023-12-14 10:49:43.274064', '2023-12-14 10:49:43.274064');
INSERT INTO users (uuid, name, nickname, password, email, bio, avatar, level, deleted, deleted_at, created_at, updated_at) VALUES ( 0x32DF6459B2004867A7C648CF47D3B45B, '1me', '1me', '123456', '1me@chilfish.top', 'hello', '/files/avatars/user_32df6459-b200-4867-a7c6-48cf47d3b45b.png', 'user', 0, null, '2023-12-14 11:11:22.022823', '2023-12-14 11:11:22.022823');
INSERT INTO users (uuid, name, nickname, password, email, bio, avatar, level, deleted, deleted_at, created_at, updated_at) VALUES ( 0x63B2BD7593114D5DB7A9AC2BF4F03F39, 'hello', 'Hello', '123456', 'me@hello.com', 'hello', '/files/avatars/user_63b2bd75-9311-4d5d-b7a9-ac2bf4f03f39.png', 'user', 0, null, '2023-12-21 14:35:40.154777', '2023-12-21 14:35:40.154777');
INSERT INTO users (uuid, name, nickname, password, email, bio, avatar, level, deleted, deleted_at, created_at, updated_at) VALUES ( 0x6CD0706377254B91974EFA5997E7873A, 'hello1703180858715', 'hello1703180858715', '123456', 'hello@hello.com', 'hello', '/files/avatars/user_6cd07063-7725-4b91-974e-fa5997e7873a.png', 'user', 0, null, '2023-12-22 01:42:28.664061', '2023-12-22 01:42:28.664061');
INSERT INTO users (uuid, name, nickname, password, email, bio, avatar, level, deleted, deleted_at, created_at, updated_at) VALUES ( 0x5992D7E63F66436F8EB6C50F6AB33827, 'blue1703217469412', 'blueee', '123456', 'blue@chilfish.top', 'hello', '/files/avatars/user_5992d7e6-3f66-436f-8eb6-c50f6ab33827.png', 'user', 0, null, '2023-12-22 11:42:56.734438', '2023-12-22 11:42:56.734438');

INSERT INTO user_status (user_id, post_count, follower_count, following_count, followers, followings) VALUES ( 1, 38, 2, 3, '["3", "4"]', '["3", "2", "5"]');
INSERT INTO user_status (user_id, post_count, follower_count, following_count, followers, followings) VALUES ( 2, 40, 3, 2, '["3", "5", "1"]', '["3", "5"]');
INSERT INTO user_status (user_id, post_count, follower_count, following_count, followers, followings) VALUES ( 3, 35, 2, 2, '["1", "2"]', '["1", "2"]');
INSERT INTO user_status (user_id, post_count, follower_count, following_count, followers, followings) VALUES ( 4, 33, 0, 2, '[]', '["5", "1"]');
INSERT INTO user_status (user_id, post_count, follower_count, following_count, followers, followings) VALUES ( 5, 27, 3, 1, '["2", "4", "1"]', '["2"]');


INSERT INTO posts (uuid, owner_id, content, deleted, deleted_at, created_at, is_body, parent_id, child_id, media) VALUES ( 0x44B0F85866DA44CAB1D3F0CA9D2C81B6, 0x6D3EDCDF59A541D6A2B3A4D85DF867AA, '  .map(::toUserDetail)
    .firstOrNull() ?: throw newError(ErrorCode.NOT_FOUND_USER)', 0, null, '2023-12-14 10:49:43.259365', 1, null, null, '[]');
INSERT INTO posts (uuid, owner_id, content, deleted, deleted_at, created_at, is_body, parent_id, child_id, media) VALUES ( 0x0F3DD62803084846AE9B1BE07E08B055, 0x6D3EDCDF59A541D6A2B3A4D85DF867AA, '  .map(::toUserDetail)
    .firstOrNull() ?: throw newError(ErrorCode.NOT_FOUND_USER)', 0, null, '2023-12-14 10:49:43.259365', 1, null, null, '[]');
INSERT INTO posts (uuid, owner_id, content, deleted, deleted_at, created_at, is_body, parent_id, child_id, media) VALUES ( 0x1D51AC7AA57C414F83FB95065ACC4CD9, 0xB3C410ADCE8F4B239127301AD5F60E76, 'eyJraWQiOiJrMSIsImFsZyI6IlJTMjU2In0.eyJleHAiOjE3MDUxMTQyMDMsImp0aSI6IkM0YUQtSnY3ZDRtM0t6ZExrX0hsdlEiLCJpYXQiOjE3MDI1MjIyMDQsInN1YiI6IntcImlkXCI6XCI2ZDNlZGNkZi01OWE1LTQxZDYtYTJiMy1hNGQ4NWRmODY3YWFcIixcIm5hbWVcIjpcIm1lXCJ9In0.ILEOA9jVddUU6jlfh7-y86Z0v36Yj62kOjluSXe_aQY6k3g-OjA4gzeJknicHccr64q5C86yn0AouaCNkje5Urc2Yq-cv8iYfgJH7Y6Ol2Wb6w5na8GU62GhFEWc5mM49nxTNCGtUemVF7bSr6zRKMocv35tVQV3gAEd2gHbqgRi_zEOMxermZmWqjD0eSLuVKqPaqAYBldSID1R8ttjTp-hOzZ1Vrv5IXkGe6Df-CnFWTsGnaoS6VZbDFFWdaA7DtPzknLlAPKtLf04xHtG92wCznySpe_T-JO1Xe9TizIlF-EQA6toKBFZCiOuvHvycJgFOKla1PMSGW8uv-YAuw', 0, null, '2023-12-14 11:11:22.011272', 1, null, null, '[]');
INSERT INTO posts (uuid, owner_id, content, deleted, deleted_at, created_at, is_body, parent_id, child_id, media) VALUES ( 0x46E347ECA6CC456A8280452AD92D7BC3, 0x32DF6459B2004867A7C648CF47D3B45B, '  .map(::toUserDetail)
    .firstOrNull() ?: throw newError(ErrorCode.NOT_FOUND_USER)', 0, null, '2023-12-14 11:11:22.011272', 1, null, null, '[]');
INSERT INTO posts (uuid, owner_id, content, deleted, deleted_at, created_at, is_body, parent_id, child_id, media) VALUES ( 0x198FDC64DCD1470FBAECA734B6E18509, 0xB3C410ADCE8F4B239127301AD5F60E76, ' opacity-0', 0, null, '2023-12-20 22:42:14.171900', 0, 0x1D51AC7AA57C414F83FB95065ACC4CD9, null, '[]');
INSERT INTO posts (uuid, owner_id, content, deleted, deleted_at, created_at, is_body, parent_id, child_id, media) VALUES ( 0x89CDECBA8FE14E84B62DB29687FB16D8, 0xB3C410ADCE8F4B239127301AD5F60E76, 'import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jws.SignatureAlgorithm;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;
import org.jose4j.jwt.ReservedClaimNames;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.RsaKeyUtil;
import org.jose4j.lang.JoseException;
', 0, null, '2023-12-20 23:30:31.838514', 1, null, null, '[]');
INSERT INTO posts (uuid, owner_id, content, deleted, deleted_at, created_at, is_body, parent_id, child_id, media) VALUES ( 0x9596233B0F5645479225F4D19389F8C7, 0xB3C410ADCE8F4B239127301AD5F60E76, '> 我一直觉得旺盛的表达和分享欲是旺盛生命力的一种体现，代表好奇和追寻，也能代表对生活细致入微的观察和思考，代表沉浸式的体验。我写了好几年推，虽状态不时有起伏，但表达欲丝毫没有减少，还挺开心的。', 0, null, '2023-12-21 13:13:57.333466', 1, null, null, '[]');
INSERT INTO posts (uuid, owner_id, content, deleted, deleted_at, created_at, is_body, parent_id, child_id, media) VALUES ( 0x4EB7F56D60BF40FF9767F009F0FA95F9, 0xB3C410ADCE8F4B239127301AD5F60E76, 'ok
', 1, '2023-12-23 14:00:31.669964', '2023-12-21 13:13:57.333466', 0, 0x89CDECBA8FE14E84B62DB29687FB16D8, null, '[]');
INSERT INTO posts (uuid, owner_id, content, deleted, deleted_at, created_at, is_body, parent_id, child_id, media) VALUES ( 0xB3B9F562ED424305BF7B4A71375C5CFE, 0x63B2BD7593114D5DB7A9AC2BF4F03F39, 'Hello there！', 0, null, '2023-12-21 14:35:40.146801', 1, null, null, '[]');
INSERT INTO posts (uuid, owner_id, content, deleted, deleted_at, created_at, is_body, parent_id, child_id, media) VALUES ( 0xC06ABD07A6494E26B8217B84BBB06A81, 0xB3C410ADCE8F4B239127301AD5F60E76, 'Hello
2', 0, null, '2023-12-22 00:54:03.405432', 0, 0xB3B9F562ED424305BF7B4A71375C5CFE, null, '[]');
INSERT INTO posts (uuid, owner_id, content, deleted, deleted_at, created_at, is_body, parent_id, child_id, media) VALUES ( 0x12BFFA556EA44FA48E16B5F9B4114DD5, 0x32DF6459B2004867A7C648CF47D3B45B, '
中气爱
今天早晨图里河已经开始大回暖，早上最低气温已经达到了-30度左右，比昨天回暖10度以上。', 0, null, '2023-12-22 11:42:56.660944', 1, null, null, '[]');
INSERT INTO posts (uuid, owner_id, content, deleted, deleted_at, created_at, is_body, parent_id, child_id, media) VALUES ( 0x9C4BEE5A603D4E2BA3065BCC02716408, 0x32DF6459B2004867A7C648CF47D3B45B, 'Visual Studio Code
@code
? GitHub Copilot just keeps getting smarter and smarter!

In case you missed them, here are 4 of the newest features that you\'re going to love ?', 0, null, '2023-12-22 11:42:56.660944', 1, null, null, '[]');
INSERT INTO posts (uuid, owner_id, content, deleted, deleted_at, created_at, is_body, parent_id, child_id, media) VALUES ( 0x59A339DB8BC74F0B99DDC011A0A20DCE, 0x6CD0706377254B91974EFA5997E7873A, 'NadeshikoManju@摇曳露营 S3 2023年四月放送
@Manjusaka_Lee
笑死，出来吃饭，听到旁边桌子有男生很大声的问：你知道 oncall 啥意思吗
（我好想站起来说我懂，我很懂', 0, null, '2023-12-22 11:42:56.660944', 1, null, null, '[]');
INSERT INTO posts (uuid, owner_id, content, deleted, deleted_at, created_at, is_body, parent_id, child_id, media) VALUES ( 0xF8950A45A25C4DD8B34415D805EDFCF3, 0x6CD0706377254B91974EFA5997E7873A, '对了，一直在用着 uid，但才发现有时会有点语义不明。例如在查 isFollowing 的时候，会有两个 uid，一个是正在查看的用户，一个是发起请求的用户。

这时候才想起“程序上下文” Context 这个概念，应该用 ctxUid 之类的变量名来代表发起请求的那个用户? ​​​', 0, null, '2023-12-22 11:42:56.660944', 1, null, null, '[]');
INSERT INTO posts (uuid, owner_id, content, deleted, deleted_at, created_at, is_body, parent_id, child_id, media) VALUES ( 0xFD536FD1CAB4448FA0B03BF875EC6128, 0x32DF6459B2004867A7C648CF47D3B45B, '看起来 win11 的 Gallery 不大能分清楚符号链接，傻傻地递归搜索出来了[二哈]明明是同一个 inode 号来着 ​​​', 0, null, '2023-12-22 11:42:56.660944', 1, null, null, '[]');
INSERT INTO posts (uuid, owner_id, content, deleted, deleted_at, created_at, is_body, parent_id, child_id, media) VALUES ( 0x7C8416CD4EB3407582363D80C54CDCC0, 0xB3C410ADCE8F4B239127301AD5F60E76, '@极游组
TV动画《迷宫饭》Netflix 中文正式预告 第二弹 公开。 2024年1月4日 开播。

Music
オープニング主題歌 「Sleep Walking Orchestra」 BUMP OF CHICKEN（TOY\'S FACTORY）
エンディング主題歌 「Party!!」 緑黄色社会（Sony Music Labels）

Cast
ライオス 熊谷健太郎
マルシル 千本木彩花
チルチャック 泊明日菜
センシ 中 博史
ファリン 早見沙織
ナマリ 三木 晶
シュロー 川田紳司
カブルー 加藤 渉
リンシャ 高橋李依
ミックベル 富田美憂
クロ 奈良 徹
ホルム 広瀬裕也
ダイア 河村 螢
シスル 小林ゆう', 0, null, '2023-12-22 11:42:56.660944', 1, null, null, '[]');
INSERT INTO posts (uuid, owner_id, content, deleted, deleted_at, created_at, is_body, parent_id, child_id, media) VALUES ( 0xB28216814BAD423A8E92C8B4DB14B980, 0x5992D7E63F66436F8EB6C50F6AB33827, 'Wow, that\'s awsome!', 0, null, '2023-12-22 11:42:56.660944', 1, null, null, '[]');
INSERT INTO posts (uuid, owner_id, content, deleted, deleted_at, created_at, is_body, parent_id, child_id, media) VALUES ( 0x58D9AA9C4F09439E95E38CCBC7E7B012, 0x5992D7E63F66436F8EB6C50F6AB33827, 'test here', 0, null, '2023-12-22 11:42:56.660944', 1, null, null, '[]');
INSERT INTO posts (uuid, owner_id, content, deleted, deleted_at, created_at, is_body, parent_id, child_id, media) VALUES ( 0x0D8223838B654198B2446B7EA612ED57, 0x32DF6459B2004867A7C648CF47D3B45B, '冷啊', 0, null, '2023-12-22 11:42:56.660944', 0, 0x12BFFA556EA44FA48E16B5F9B4114DD5, null, '[]');
INSERT INTO posts (uuid, owner_id, content, deleted, deleted_at, created_at, is_body, parent_id, child_id, media) VALUES ( 0x7310DEDFAA7B4AE4A05B2EAD1958D103, 0x5992D7E63F66436F8EB6C50F6AB33827, 'Ehco
@Ehco1996
分享后端开发 side project 迷思

精心构建每一个 db query，保证一定能命中索引 不全表扫描

较劲脑汁设计每一个 api，减少的外 io，能异步一定上队列，让每个请求都快到极致

项目上线半年后，由于界面画的太丑没人用，db 里翻来覆去也只有几百条记录，优化器说走什么索引，全拉进内存就完事了', 0, null, '2023-12-22 11:42:56.660944', 1, null, null, '[]');
INSERT INTO posts (uuid, owner_id, content, deleted, deleted_at, created_at, is_body, parent_id, child_id, media) VALUES ( 0x2E720A65C8CF45C8BCE631ACE54E4562, 0xB3C410ADCE8F4B239127301AD5F60E76, 'TVアニメ『君のことが大大大大大好きな100人の彼女』公式
@hyakkano_anime
／
     #アニメ100カノ
　 ❤グッズ情報❤
＼

グッズページに商品を追加しました❣

ふわふわクッションの上で眠っている彼女たちの描き起こしイラストを使用したグッズが登場??

・トレーディングふわみん缶バッジ
・トレーディングふわみんアクリルスタンド

詳細はこちら✨
https://hyakkano.com/goods', 0, null, '2023-12-22 11:42:56.660944', 1, null, null, '[]');
INSERT INTO posts (uuid, owner_id, content, deleted, deleted_at, created_at, is_body, parent_id, child_id, media) VALUES ( 0xC8DFFEAC5E3B454BAE35E3F961D85E91, 0x32DF6459B2004867A7C648CF47D3B45B, 'Anthony Fu
@antfu7
Reminder that we will do live streaming today, the last one before the holiday! And it comes to the 10th episode, amazing how time goes! ?

See you in an hour!
youtube.com
[Scheduled] Let\'s build Nuxt playground! Episode 10
Trailer & Introduction: https://youtu.be/Jh-jPx5ef8gEpisodes Playlist: https://www.youtube.com/playlist?list=PL4ETc_mXFfxUGiY852jH3ctljnI2e9RaxGitHub Repo: h...', 0, null, '2023-12-22 11:42:56.660944', 1, null, null, '[]');
INSERT INTO posts (uuid, owner_id, content, deleted, deleted_at, created_at, is_body, parent_id, child_id, media) VALUES ( 0x7A341BD52A264DEB986FDE25EDAE13B5, 0xB3C410ADCE8F4B239127301AD5F60E76, '用 Python 发送 HTTP 请求，1行 requests 有很多问题，比如如果对方服务挂了，程序就永远 hang 在发送请求的地方了。

这段 snippet 带有重试，连接保持，超时，基本上所有发送 HTTP 的地方都要这么做。

https://gist.github.com/laixintao/e9eae48a835e741969ae06af3ad45f71', 0, null, '2023-12-22 11:42:56.660944', 1, null, null, '[]');
INSERT INTO posts (uuid, owner_id, content, deleted, deleted_at, created_at, is_body, parent_id, child_id, media) VALUES ( 0x525296777BBF40C1A791B162D704A5A6, 0xB3C410ADCE8F4B239127301AD5F60E76, '?‍?️?‍?️?‍?️?‍?️?‍?️', 0, null, '2023-12-22 12:13:50.437256', 0, 0x12BFFA556EA44FA48E16B5F9B4114DD5, null, '[]');
INSERT INTO posts (uuid, owner_id, content, deleted, deleted_at, created_at, is_body, parent_id, child_id, media) VALUES ( 0x04A4CF525DC24952B5354E2EBF88D962, 0xB3C410ADCE8F4B239127301AD5F60E76, '这个内容技术性不强，但听听管理者视角讲述一些东西，也挺有意思，凯瑟琳透露了几点：
1.2024年计划建成星城二号发射塔。
2.目前在申请星舰第三次和第四次发射的许可。
3.2024年希望测试重型助推器着陆。
4.在和FAA讨论，能否一次申请多个许可。', 0, null, '2023-12-22 15:38:06.345800', 1, null, null, '[]');
INSERT INTO posts (uuid, owner_id, content, deleted, deleted_at, created_at, is_body, parent_id, child_id, media) VALUES ( 0x08533AE9153D4EBD894CC5141F06D690, 0xB3C410ADCE8F4B239127301AD5F60E76, 'suki!', 1, '2023-12-23 13:53:46.994496', '2023-12-22 15:38:06.345800', 0, 0x2E720A65C8CF45C8BCE631ACE54E4562, null, '[]');
INSERT INTO posts (uuid, owner_id, content, deleted, deleted_at, created_at, is_body, parent_id, child_id, media) VALUES ( 0x4928128459E249019797554FFA2C8BF3, 0xB3C410ADCE8F4B239127301AD5F60E76, '体感 -1 度了今天???', 0, null, '2023-12-22 15:38:06.345800', 0, 0x0D8223838B654198B2446B7EA612ED57, null, '[]');
INSERT INTO posts (uuid, owner_id, content, deleted, deleted_at, created_at, is_body, parent_id, child_id, media) VALUES ( 0xCF1E6E4F134243C4981CA7CA17D2B3A2, 0xB3C410ADCE8F4B239127301AD5F60E76, '有哪些翻译错误却成为经典？直达问题
发布于 2023-11-08 07:21
你关注的 琴梨梨OvO 赞同

Android的中文译名，安卓。

其实Android这个词作为谷歌的一个商标产品，是有官方正式的名称的，叫安致。

最早Android进入中国的时候，并没有官方正式中文名称，刚开始是民间的爱好者为推广使用主力，那时有很多相关的网站和论坛，比如安卓网，这时安卓只是民间爱好者根据Android的读音自发接受的一个非正式中文译名。

后来不久后李开复负责谷歌中国，Android需要走到正式台面上来，也需要一个官方正式的推广和发布，就组织各路媒体还开了个正式的发布会，

“我们唱着歌、跳着舞，带着安致走进中国来”。

谷歌官方就算正式把安致作为Android的官方正式中文译名发布并盖章确认了。

然并卵，老百姓不吃这套，安卓叫起来好久了，也都习惯了，而且刷机、找ROM、找软件（那时几乎还没有软件市场、应用商店的概念，有也只是雏形和一些简陋的草台班子，其实那时Android上软件也都没多少），都习惯于上安卓网了，你非要弄出来个安致，虽然是正统、官方确认的，可就是叫起来不顺口、不习惯啊。

所以，现在，谁都知道安卓，有几个还知道安致的了？', 1, '2023-12-23 00:03:18.456428', '2023-12-22 17:11:09.430743', 1, null, null, '[]');
INSERT INTO posts (uuid, owner_id, content, deleted, deleted_at, created_at, is_body, parent_id, child_id, media) VALUES ( 0xAB9FA2C107334E1BAFAD7F380CFF1183, 0xB3C410ADCE8F4B239127301AD5F60E76, 'http://localhost:3000/@chilfish/cf1e6e4f-1342-43c4-981c-a7ca17d2b3a2', 0, null, '2023-12-22 17:11:09.430743', 0, 0xCF1E6E4F134243C4981CA7CA17D2B3A2, null, '[]');
INSERT INTO posts (uuid, owner_id, content, deleted, deleted_at, created_at, is_body, parent_id, child_id, media) VALUES ( 0x609CFFF5964A4A5DA76101CEF20FEDE8, 0xB3C410ADCE8F4B239127301AD5F60E76, '好麻烦啊……', 1, '2023-12-23 00:00:01.594700', '2023-12-22 19:07:02.412764', 1, null, null, '[]');
INSERT INTO posts (uuid, owner_id, content, deleted, deleted_at, created_at, is_body, parent_id, child_id, media) VALUES ( 0x730B4EFE2AA44DEF8A4D03690EBBF8CB, 0x5992D7E63F66436F8EB6C50F6AB33827, 'Where is your parents?', 1, '2023-12-23 00:41:39.884247', '2023-12-23 00:36:01.153335', 0, 0xAB9FA2C107334E1BAFAD7F380CFF1183, null, '[]');
INSERT INTO posts (uuid, owner_id, content, deleted, deleted_at, created_at, is_body, parent_id, child_id, media) VALUES ( 0xD211C264FDBE42EF914EA932E2E4AD01, 0x5992D7E63F66436F8EB6C50F6AB33827, 'Good', 0, null, '2023-12-23 00:36:01.153335', 0, 0x730B4EFE2AA44DEF8A4D03690EBBF8CB, null, '[]');
INSERT INTO posts (uuid, owner_id, content, deleted, deleted_at, created_at, is_body, parent_id, child_id, media) VALUES ( 0x71D55041F18348F1890D33FB655D66A4, 0xB3C410ADCE8F4B239127301AD5F60E76, 'test', 1, '2023-12-23 02:16:09.048421', '2023-12-23 02:15:40.721125', 0, 0x04A4CF525DC24952B5354E2EBF88D962, null, '[]');
INSERT INTO posts (uuid, owner_id, content, deleted, deleted_at, created_at, is_body, parent_id, child_id, media) VALUES ( 0x3CAA333518F945B9828650A0D860F09D, 0xB3C410ADCE8F4B239127301AD5F60E76, 'test', 1, '2023-12-23 13:31:07.175413', '2023-12-23 02:26:41.059617', 0, 0x7A341BD52A264DEB986FDE25EDAE13B5, null, '[]');
INSERT INTO posts (uuid, owner_id, content, deleted, deleted_at, created_at, is_body, parent_id, child_id, media) VALUES ( 0xBCF287CAD25C42B98DD371E50161033F, 0xB3C410ADCE8F4B239127301AD5F60E76, '删除推文与评论，以及对应UI，完成???', 0, null, '2023-12-23 13:58:09.379851', 1, null, null, '[]');
INSERT INTO posts (uuid, owner_id, content, deleted, deleted_at, created_at, is_body, parent_id, child_id, media) VALUES ( 0x68D9772A82CE4E84A6E791AC5C32294C, 0x5992D7E63F66436F8EB6C50F6AB33827, 'Good job', 0, null, '2023-12-23 14:09:07.361394', 0, 0xAB9FA2C107334E1BAFAD7F380CFF1183, null, '[]');
INSERT INTO posts (uuid, owner_id, content, deleted, deleted_at, created_at, is_body, parent_id, child_id, media) VALUES ( 0xD6688FE7D1294FADB4C2D66FD26990CF, 0xB3C410ADCE8F4B239127301AD5F60E76, '这个问题是由于在运行构建的JAR文件时，文件路径的格式不正确导致的。在JAR文件中，资源文件被打包在JAR文件的内部，因此无法直接使用文件路径来访问它们。

要解决这个问题，您可以使用`ClassLoader`来获取资源文件的输入流。这样可以确保在JAR文件中正确获取资源文件。

```kotlin
import org.springframework.core.io.ClassPathResource
import org.springframework.util.StreamUtils
import java.nio.file.Files
import java.nio.file.Paths

fun uploadImage(imgFile: MultipartFile, path: String, fileName: String): String {
    val jpgImg = convertToJpg(imgFile)
    val resource = ClassPathResource("static/avatars/$fileName")
    val dest = Paths.get(resource.uri)
    Files.write(dest, jpgImg)

    return "/$path/$fileName"
}
```

在上面的代码中，我们使用`ClassPathResource`来获取资源文件的路径，并使用`Paths.get(resource.uri)`将其转换为`Path`对象。然后，我们使用`Files.write()`将字节数组写入目标文件。

请注意，上述代码假设`static`文件夹位于类路径的根目录下。如果`static`文件夹位于其他位置，请相应地更改资源路径。

使用上述代码，您应该能够在运行构建的JAR文件时正确保存和读取文件。', 0, null, '2023-12-23 14:48:35.727568', 0, 0x68D9772A82CE4E84A6E791AC5C32294C, null, '[]');
INSERT INTO posts (uuid, owner_id, content, deleted, deleted_at, created_at, is_body, parent_id, child_id, media) VALUES ( 0x637E35A285DA48909FFCFAF1F569DC0D, 0xB3C410ADCE8F4B239127301AD5F60E76, '长文也能 crop 裁切了', 0, null, '2023-12-23 15:10:25.736674', 1, null, null, '[]');
INSERT INTO posts (uuid, owner_id, content, deleted, deleted_at, created_at, is_body, parent_id, child_id, media) VALUES ( 0x72BC3AF5FDA348B09AD92D5C77B63A03, 0xB3C410ADCE8F4B239127301AD5F60E76, '试试', 0, null, '2023-12-23 15:10:25.736674', 0, 0xBCF287CAD25C42B98DD371E50161033F, null, '[]');
INSERT INTO posts (uuid, owner_id, content, deleted, deleted_at, created_at, is_body, parent_id, child_id, media) VALUES ( 0xBA9AE66886764882BE63A569BA1DD4C5, 0xB3C410ADCE8F4B239127301AD5F60E76, 'test', 0, null, '2023-12-23 15:27:43.933238', 1, null, null, '[]');
INSERT INTO posts (uuid, owner_id, content, deleted, deleted_at, created_at, is_body, parent_id, child_id, media) VALUES ( 0x939CE90365A14824B1FA471B3F18437E, 0x6CD0706377254B91974EFA5997E7873A, '
蛋挞也是 Taco?
@ytiihyslqlabpow
没有灵感，写不出 2023 年终总结……

前端之?面包包
@himself_65
不写了 睡大觉', 0, null, '2023-12-23 15:29:29.672824', 1, null, null, '[]');
INSERT INTO posts (uuid, owner_id, content, deleted, deleted_at, created_at, is_body, parent_id, child_id, media) VALUES ( 0x1B72F5B04320431C9D3E52CE3AC7462A, 0x6CD0706377254B91974EFA5997E7873A, '哈哈', 0, null, '2023-12-23 15:29:29.672824', 0, 0x59A339DB8BC74F0B99DDC011A0A20DCE, null, '[]');
INSERT INTO posts (uuid, owner_id, content, deleted, deleted_at, created_at, is_body, parent_id, child_id, media) VALUES ( 0xEDA000FE7E0247538C8346245C421483, 0xB3C410ADCE8F4B239127301AD5F60E76, '你说得对，但是', 0, null, '2023-12-23 19:30:25.976240', 0, 0xF8950A45A25C4DD8B34415D805EDFCF3, null, '[]');


INSERT INTO post_status (post_id, like_count, comment_count, repost_count, likes) VALUES ( 1, 0, 0, 0, '[]');
INSERT INTO post_status (post_id, like_count, comment_count, repost_count, likes) VALUES ( 2, 0, 0, 0, '[]');
INSERT INTO post_status (post_id, like_count, comment_count, repost_count, likes) VALUES ( 3, 2, 1, 0, '["1", "3"]');
INSERT INTO post_status (post_id, like_count, comment_count, repost_count, likes) VALUES ( 4, 0, 0, 0, '[]');
INSERT INTO post_status (post_id, like_count, comment_count, repost_count, likes) VALUES ( 5, 0, 0, 0, '[]');
INSERT INTO post_status (post_id, like_count, comment_count, repost_count, likes) VALUES ( 6, 0, 0, 0, '[]');
INSERT INTO post_status (post_id, like_count, comment_count, repost_count, likes) VALUES ( 7, 1, 0, 0, '["1"]');
INSERT INTO post_status (post_id, like_count, comment_count, repost_count, likes) VALUES ( 8, 1, 0, 0, '["1"]');
INSERT INTO post_status (post_id, like_count, comment_count, repost_count, likes) VALUES ( 9, 0, 1, 0, '[]');
INSERT INTO post_status (post_id, like_count, comment_count, repost_count, likes) VALUES ( 10, 1, 0, 0, '["1"]');
INSERT INTO post_status (post_id, like_count, comment_count, repost_count, likes) VALUES ( 11, 2, 2, 0, '["1", "5"]');
INSERT INTO post_status (post_id, like_count, comment_count, repost_count, likes) VALUES ( 12, 0, 0, 0, '[]');
INSERT INTO post_status (post_id, like_count, comment_count, repost_count, likes) VALUES ( 13, 0, 1, 0, '[]');
INSERT INTO post_status (post_id, like_count, comment_count, repost_count, likes) VALUES ( 14, 1, 1, 0, '["1"]');
INSERT INTO post_status (post_id, like_count, comment_count, repost_count, likes) VALUES ( 15, 0, 0, 0, '[]');
INSERT INTO post_status (post_id, like_count, comment_count, repost_count, likes) VALUES ( 16, 0, 0, 0, '[]');
INSERT INTO post_status (post_id, like_count, comment_count, repost_count, likes) VALUES ( 17, 0, 0, 0, '[]');
INSERT INTO post_status (post_id, like_count, comment_count, repost_count, likes) VALUES ( 18, 1, 0, 0, '["1"]');
INSERT INTO post_status (post_id, like_count, comment_count, repost_count, likes) VALUES ( 19, 1, 1, 0, '["1"]');
INSERT INTO post_status (post_id, like_count, comment_count, repost_count, likes) VALUES ( 20, 0, 0, 0, '[]');
INSERT INTO post_status (post_id, like_count, comment_count, repost_count, likes) VALUES ( 21, 1, 1, 0, '["1"]');
INSERT INTO post_status (post_id, like_count, comment_count, repost_count, likes) VALUES ( 22, 0, 0, 0, '[]');
INSERT INTO post_status (post_id, like_count, comment_count, repost_count, likes) VALUES ( 23, 0, 0, 0, '[]');
INSERT INTO post_status (post_id, like_count, comment_count, repost_count, likes) VALUES ( 24, 0, 0, 0, '[]');
INSERT INTO post_status (post_id, like_count, comment_count, repost_count, likes) VALUES ( 25, 0, 1, 0, '[]');
INSERT INTO post_status (post_id, like_count, comment_count, repost_count, likes) VALUES ( 26, 1, 0, 0, '["1"]');
INSERT INTO post_status (post_id, like_count, comment_count, repost_count, likes) VALUES ( 27, 0, 0, 0, '[]');
INSERT INTO post_status (post_id, like_count, comment_count, repost_count, likes) VALUES ( 28, 0, 1, 0, '[]');
INSERT INTO post_status (post_id, like_count, comment_count, repost_count, likes) VALUES ( 29, 1, 2, 0, '["5"]');
INSERT INTO post_status (post_id, like_count, comment_count, repost_count, likes) VALUES ( 30, 1, 0, 0, '["1"]');
INSERT INTO post_status (post_id, like_count, comment_count, repost_count, likes) VALUES ( 31, 0, 1, 0, '[]');
INSERT INTO post_status (post_id, like_count, comment_count, repost_count, likes) VALUES ( 32, 0, 0, 0, '[]');
INSERT INTO post_status (post_id, like_count, comment_count, repost_count, likes) VALUES ( 33, 0, 0, 0, '[]');
INSERT INTO post_status (post_id, like_count, comment_count, repost_count, likes) VALUES ( 34, 0, 0, 0, '[]');
INSERT INTO post_status (post_id, like_count, comment_count, repost_count, likes) VALUES ( 35, 2, 1, 0, '["1", "4"]');
INSERT INTO post_status (post_id, like_count, comment_count, repost_count, likes) VALUES ( 36, 0, 1, 0, '[]');
INSERT INTO post_status (post_id, like_count, comment_count, repost_count, likes) VALUES ( 37, 0, 0, 0, '[]');
INSERT INTO post_status (post_id, like_count, comment_count, repost_count, likes) VALUES ( 38, 1, 0, 0, '["4"]');
INSERT INTO post_status (post_id, like_count, comment_count, repost_count, likes) VALUES ( 39, 0, 0, 0, '[]');
INSERT INTO post_status (post_id, like_count, comment_count, repost_count, likes) VALUES ( 40, 0, 0, 0, '[]');
INSERT INTO post_status (post_id, like_count, comment_count, repost_count, likes) VALUES ( 41, 2, 0, 0, '["4", "1"]');
INSERT INTO post_status (post_id, like_count, comment_count, repost_count, likes) VALUES ( 42, 0, 0, 0, '[]');
INSERT INTO post_status (post_id, like_count, comment_count, repost_count, likes) VALUES ( 43, 0, 0, 0, '[]');
