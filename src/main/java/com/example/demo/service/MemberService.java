package com.example.demo.service;


import com.example.demo.domain.Member;
import com.example.demo.repository.MemberJdbcRepository;
import com.example.demo.repository.MemberMybatisRepository;
import com.example.demo.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class MemberService {

//    service에서 repository를 주입받기 위해서, Autowired를 사용
    //    SpringDataJpa를 사용한 repository
    @Autowired
    private MemberRepository memberRepository;

//    0521 추가
    // mybatis를 사용한 repository
    //jpa와 함께 사용할 수도 있다. 복잡한 service logic 또는 heavy한 쿼리가 있을 경우
    //jpa로는 한계가 있으므로, 현업에서는 mybatis와 jpa를 섞어 사용하기도 한다
    //현대적인 데이터베이스 커넥팅은 jpa+로우쿼리신기술들이긴 함
    @Autowired
    private MemberMybatisRepository memberMybatisRepository;

    @Autowired
    private MemberJdbcRepository memberJdbcRepository;


//    회원가입
    public void create(Member member){
        memberRepository.save(member);
    }
//    public void create(Member member) throws SQLException {
//        memberJdbcRepository.save(member);
//    }



//    회원목록조회
//    memberRepository.findAll()의 기본 return 타입은 List<해당객체>
    public List<Member> findAll(){
//        Jpa레파지토리에서는 리턴타입이 기본적으로 옵셔널이기 때문에 orElse 붙여줘야함
        List<Member> members = memberRepository.findAll();  //.orElse(null);
//        List<Member> members = memberMybatisRepository.findAll();
        return members;
    }

//    throws SQLException는 jdbc 쓸 때 붙여둔 것
//    이 예외처리가 강제되는 이유
//    spring과 java는 별개의 프로세스
//    spring입장에서 mySql서버가 뻗어있을지 뻗어있지 않은지 알 수 없는 경우가 많음 -> 강제시키는 경우가 있다!
//    spring이 아니라 타 서버를 조회하게 될 때 이 예외처리가 강제되는 경우가 종종 있다... 정도만 기억하면 될 듯
    public Member findById(Long id) throws SQLException{
        Member members = memberRepository.findById(id).orElse(null);
//        Member members = memberMybatisRepository.findById(id);
//        Member members = memberJdbcRepository.findById(id);
        return members;
    }


//    회원수정
    public void update(Member member) throws Exception {
//        member1조회할 때 아이디, 이름, 이메일, 비번을 이미 끌어오므로 밑에 setId를 하지 않아도 됨
        Member member1 = memberRepository.findById(member.getId()).orElse(null);
        if(member1==null){
            throw new Exception();
        }else{
            member1.setName(member.getName());
            member1.setEmail(member.getEmail());
            member1.setPassword(member.getPassword());
//        [외워두기] save는 이미 존재하는 pk(id)가 있으면 update로 동작, id값이 없으면 insert로 동작
//        그래서 예외처리 굳이 안 해도 작동은 되지만, 일반적인 방법은 아니다.(예외처리하기)
            memberRepository.save(member);
        }
//        원래 pk는 보통 사용자에게 공개하지 않음(수정불가이므로?) Id를 아예 끌어오지도 않음. findByEmail등을 사용해 조회하게됨



    }
}
