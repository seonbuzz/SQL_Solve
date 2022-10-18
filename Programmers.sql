-- Programmers 조건에 맞는 회원 수 구하기
-- USER_INFO 테이블에서 2021년에 가입한 회원 중 나이가 20세 이상 29세 이하인 회원이 몇 명인지 출력하는 SQL문을 작성해주세요.
SELECT COUNT(*)
FROM USER_INFO
WHERE YEAR(JOINED) = 2021
AND AGE BETWEEN 20 AND 29;

-- Count = 97