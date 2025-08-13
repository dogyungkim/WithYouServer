package UMC.WithYou.notice;

import UMC.WithYou.feature.member.domain.Member;
import UMC.WithYou.feature.notice.domain.Notice;
import UMC.WithYou.feature.notice.domain.NoticeCheck;
import UMC.WithYou.feature.travel.domain.TravelStatus;
import UMC.WithYou.feature.travel.domain.Travel;

import java.lang.reflect.Field;

/**
 * Notice 객체를 테스트에서 쉽게 생성하기 위한 Fixture 클래스
 */
public class NoticeFixture {

    /**
     * 기본 Notice 객체를 생성합니다.
     * @param member 공지사항 작성자
     * @param travel 공지사항이 속한 여행
     * @return 기본값으로 설정된 Notice 객체
     */
    public static Notice createDefaultNotice(Member member, Travel travel) {
        return Notice.builder()
                .content("테스트 공지사항입니다.")
                .status(TravelStatus.BEFORE) // 여행 전
                .member(member)
                .travel(travel)
                .build();
    }
    
    /**
     * ID가 설정된 Notice 객체를 생성합니다.
     * @param id 설정할 ID 값
     * @param member 공지사항 작성자
     * @param travel 공지사항이 속한 여행
     * @return ID가 설정된 Notice 객체
     */
    public static Notice createNoticeWithId(Long id, Member member, Travel travel) {
        Notice notice = createDefaultNotice(member, travel);
        setNoticeId(notice, id);
        return notice;
    }
    
    /**
     * 사용자 정의 정보를 가진 Notice 객체를 생성합니다.
     * @param content 공지사항 내용
     * @param state 공지사항 상태 (0: 여행 전, 1: 여행 중, 2: 여행 후)
     * @param member 공지사항 작성자
     * @param travel 공지사항이 속한 여행
     * @return 사용자 정의 정보로 설정된 Notice 객체
     */
    public static Notice createCustomNotice(String content, TravelStatus status, Member member, Travel travel) {
        return Notice.builder()
                .content(content)
                .status(status)
                .member(member)
                .travel(travel)
                .build();
    }
    
    /**
     * 사용자 정의 정보와 ID를 가진 Notice 객체를 생성합니다.
     * @param id 설정할 ID 값
     * @param content 공지사항 내용
     * @param state 공지사항 상태 (0: 여행 전, 1: 여행 중, 2: 여행 후)
     * @param member 공지사항 작성자
     * @param travel 공지사항이 속한 여행
     * @return 사용자 정의 정보와 ID로 설정된 Notice 객체
     */
    public static Notice createCustomNoticeWithId(Long id, String content, TravelStatus status, Member member, Travel travel) {
        Notice notice = createCustomNotice(content, status, member, travel);
        setNoticeId(notice, id);
        return notice;
    }
    
    /**
     * Notice 객체의 ID를 설정합니다 (리플렉션 사용).
     * @param notice ID를 설정할 Notice 객체
     * @param id 설정할 ID 값
     */
    public static void setNoticeId(Notice notice, Long id) {
        try {
            Field idField = Notice.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(notice, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Notice ID 설정 중 에러 발생: " + e.getMessage(), e);
        }
    }
    
    /**
     * 기본 NoticeCheck 객체를 생성합니다.
     * @param member 공지사항을 확인한 회원
     * @param notice 확인된 공지사항
     * @return 기본값으로 설정된 NoticeCheck 객체
     */
    public static NoticeCheck createDefaultNoticeCheck(Member member, Notice notice) {
        return NoticeCheck.builder()
                .isChecked(false)
                .member(member)
                .notice(notice)
                .build();
    }
    
    /**
     * ID가 설정된 NoticeCheck 객체를 생성합니다.
     * @param id 설정할 ID 값
     * @param member 공지사항을 확인한 회원
     * @param notice 확인된 공지사항
     * @return ID가 설정된 NoticeCheck 객체
     */
    public static NoticeCheck createNoticeCheckWithId(Long id, Member member, Notice notice) {
        NoticeCheck noticeCheck = createDefaultNoticeCheck(member, notice);
        setNoticeCheckId(noticeCheck, id);
        return noticeCheck;
    }
    
    /**
     * 사용자 정의 정보를 가진 NoticeCheck 객체를 생성합니다.
     * @param isChecked 확인 여부
     * @param member 공지사항을 확인한 회원
     * @param notice 확인된 공지사항
     * @return 사용자 정의 정보로 설정된 NoticeCheck 객체
     */
    public static NoticeCheck createCustomNoticeCheck(boolean isChecked, Member member, Notice notice) {
        return NoticeCheck.builder()
                .isChecked(isChecked)
                .member(member)
                .notice(notice)
                .build();
    }
    
    /**
     * NoticeCheck 객체의 ID를 설정합니다 (리플렉션 사용).
     * @param noticeCheck ID를 설정할 NoticeCheck 객체
     * @param id 설정할 ID 값
     */
    public static void setNoticeCheckId(NoticeCheck noticeCheck, Long id) {
        try {
            Field idField = NoticeCheck.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(noticeCheck, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("NoticeCheck ID 설정 중 에러 발생: " + e.getMessage(), e);
        }
    }
} 