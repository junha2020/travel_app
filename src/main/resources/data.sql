-- 1. 도쿄 (Tokyo) 대표 랜드마크 4선
INSERT IGNORE INTO place (id, name, address, latitude, longitude, image_url, description, category, rating)
VALUES
(1, '도쿄 타워', '일본 도쿄도 미나토구 시바코엔 4-2-8', 35.6586, 139.7454,
 'https://images.unsplash.com/photo-1503899036084-c55cdd92da26?auto=format&fit=crop&w=800&q=80',
 '도쿄의 영원한 상징이자 아름다운 주홍빛 전망대. 밤이 되면 눈부신 조명 쇼가 펼쳐집니다.',
 '관광지', 4.8),
(2, '센소지', '일본 도쿄도 다이토구 아사쿠사 2-3-1', 35.7148, 139.7967,
 'https://images.unsplash.com/photo-1493976040374-85c8e12f0c0e?auto=format&fit=crop&w=800&q=80',
 '도쿄에서 가장 오래된 유서 깊은 사찰. 붉은 제등 카미나리몬이 대표적인 포토 스팟입니다.',
 '관광지', 4.5),
(3, '신주쿠 교엔', '일본 도쿄도 신주쿠구 내藤마치 11', 35.6852, 139.7101,
 'https://images.unsplash.com/photo-1524413840807-0c3cb6fa808d?auto=format&fit=crop&w=800&q=80',
 '도심 정가운데 위치한 드넓은 황실 정원. 사계절 벚꽃과 단풍을 감상하며 조용한 힐링이 가능합니다.',
 '자연', 4.6),
(4, '시부야 스카이', '일본 도쿄도 시부야구 시부야 2-24-12', 35.6585, 139.7018,
 'https://images.unsplash.com/photo-1540959733332-eab4deceeaf7?auto=format&fit=crop&w=800&q=80',
 '시부야 스크램블 스퀘어 옥상의 초고층 루프탑 전망대. 도쿄 전역을 발밑으로 굽어보는 야경 명소입니다.',
 '관광지', 4.9);

-- 2. 오사카 (Osaka) 대표 랜드마크 4선
INSERT IGNORE INTO place (id, name, address, latitude, longitude, image_url, description, category, rating)
VALUES
(5, '도톤보리', '일본 오사카부 오사카시 주오구 도톤보리', 34.6687, 135.5013,
 'https://images.unsplash.com/photo-1590250767139-4d6b63ca44be?auto=format&fit=crop&w=800&q=80',
 '오사카의 심장이자 식도락의 천국. 상징적인 글리코상 네온사인과 화려한 도심 맛집들이 가득합니다.',
 '관광지', 4.8),
(6, '오사카 성', '일본 오사카부 오사카시 주오구 오사카조 1-1', 34.6873, 135.5262,
 'https://images.unsplash.com/photo-1542640244-7e672d6cef21?auto=format&fit=crop&w=800&q=80',
 '도심 속 녹음으로 둘러싸인 역사적 성곽. 봄에는 벚꽃, 가을에는 단풍이 아주 장관을 이룹니다.',
 '관광지', 4.6),
(7, '유니버셜 스튜디오 재팬 (USJ)', '일본 오사카부 오사카시 고노하나구 사쿠라지마 2-1-33', 34.6654, 135.4323,
 'https://images.unsplash.com/photo-1616788494707-ec28f08d05a1?auto=format&fit=crop&w=800&q=80',
 '해리포터, 닌텐도 월드 등 스펙터클한 영화 테마가 가득해 평생 잊지 못할 추억을 만드는 테마파크입니다.',
 '관광지', 4.9),
(8, '아베노 하루카스 300', '일본 오사카부 오사카시 아베노구 아베노스지 1-1-43', 34.6458, 135.5139,
 'https://images.unsplash.com/photo-1615814299905-2bcf8191cbba?auto=format&fit=crop&w=800&q=80',
 '일본에서 가장 높은 초고층 빌딩의 360도 유리창 전망대. 숨 막히게 아름다운 도시 지평선을 자랑합니다.',
 '관광지', 4.7);

-- 3. 후쿠오카 (Fukuoka) 대표 랜드마크 4선
INSERT IGNORE INTO place (id, name, address, latitude, longitude, image_url, description, category, rating)
VALUES
(9, '후쿠오카 타워', '일본 후쿠오카현 후쿠오카시 사와라구 모모치하마 2-3-26', 33.5932, 130.3515,
 'https://images.unsplash.com/photo-1590250555776-63e80cc0be5b?auto=format&fit=crop&w=800&q=80',
 '8000여 장의 거울로 덮여 바다를 반사하는 전망타워. 밤이면 화려한 계절별 일루미네이션이 켜집니다.',
 '관광지', 4.5),
(10, '모모치 해변공원', '일본 후쿠오카현 후쿠오카시 사와라구 모모치하마 2', 33.5954, 130.3524,
 'https://images.unsplash.com/photo-1601042879364-f3947d3f9c16?auto=format&fit=crop&w=800&q=80',
 '하얀 백사장과 이국적인 마리존 리조트 건물이 어우러진 노을 맛집 해변 산책 코스입니다.',
 '자연', 4.4),
(11, '오호리 공원', '일본 후쿠오카현 후쿠오카시 주오구 오호리코엔 1', 33.5859, 130.3764,
 'https://images.unsplash.com/photo-1563245372-f21724e3856d?auto=format&fit=crop&w=800&q=80',
 '후쿠오카 성터를 본떠 만든 큰 도심 호수 공원. 잔잔한 수면을 감상하며 걷기 좋습니다.',
 '자연', 4.6),
(12, '캐널시티 하카타', '일본 후쿠오카현 후쿠오카시 주오구 스미요시 1-2', 33.5898, 130.4108,
 'https://images.unsplash.com/photo-1503899036084-c55cdd92da26?auto=format&fit=crop&w=800&q=80',
 '180m의 운하를 따라 화려한 분수 쇼가 열리는 곳. 라멘 스타디움 등 다양한 즐길 거리가 가득합니다.',
 '관광지', 4.5);

-- 4. 삿포로 (Sapporo) 대표 랜드마크 4선
INSERT IGNORE INTO place (id, name, address, latitude, longitude, image_url, description, category, rating)
VALUES
(13, '오도리 공원', '일본 홋카이도 삿포로시 주오구 오도리니시 1-12', 43.0605, 141.3468,
 'https://images.unsplash.com/photo-1542931287-023b922fa89b?auto=format&fit=crop&w=800&q=80',
 '삿포로 중심부를 동서로 가르는 공원. 겨울철 눈 축제와 여름철 거대 맥주 정원이 활성화됩니다.',
 '자연', 4.5),
(14, '삿포로 TV 타워', '일본 홋카이도 삿포로시 주오구 오도리니시 1', 43.0611, 141.3564,
 'https://images.unsplash.com/photo-1578328819058-b69f3a3b0f6b?auto=format&fit=crop&w=800&q=80',
 '오도리 공원 끝에 우뚝 솟아 아름다운 삿포로 도심 경치를 선물해 주는 시그니처 랜드마크 타워입니다.',
 '관광지', 4.4),
(15, '시로이 코이비토 파크', '일본 홋카이도 삿포로시 니시구 미야노사와 2-2-11-36', 43.0881, 141.2713,
 'https://images.unsplash.com/photo-1570168007204-dfb528c6958f?auto=format&fit=crop&w=800&q=80',
 '삿포로의 명물 과자 시로이 코이비토 공장 겸 초콜릿 테마파크. 영국식 정원이 이채롭습니다.',
 '관광지', 4.6),
(16, '모이와야마 전망대', '일본 홋카이도 삿포로시 미나미구 모이와야마 1917', 43.0232, 141.3339,
 'https://images.unsplash.com/photo-1518156677180-95a2893f3e9f?auto=format&fit=crop&w=800&q=80',
 '홋카이도의 신 3대 도시 야경을 굽어보는 최고의 데이트 코스 전망대입니다.',
 '자연', 4.8);