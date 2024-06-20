const filterItemsData: Array<any> = [
  {
    label: "최종학력",
    value: "degrees",
    list: ["pPollSize", "MASTER", "BACHELOR"],
    addAble: false,
    isOption: true,
    identifier: "",
  },
  {
    label: "column1",
    value: "column2",
    list: [
      // "한국", "미국", "캐나다"
    ],
    addAble: true,
    isOption: true,
    identifier: "COUNTRY",
  },
  {
    label: "직급",
    value: "jobRanks",
    list: [],
    addAble: true,
    isOption: true,
    identifier: "JOBRANK",
  },
  {
    label: "기업",
    value: "companyNames",
    list: [],
    addAble: true,
    isOption: true,
    identifier: "COMPANY",
  },
  {
    label: "학교명",
    value: "schoolNames",
    list: [
      // "서울대학교", "서강대학교", "서경대학교"
    ],
    addAble: true,
    isOption: true,
    identifier: "SCHOOL",
  },
  // {
  //   label: "진행상황",
  //   value: "employStatus",
  //   list: [
  //     "NONE", // 전체
  //     "FOUND", // 발굴 완료
  //     "USAGE_REVIEW", // 활용도 검토
  //     "CONTACT", // 접촉
  //     "SHORT_TERM_MANAGE", // 단기관리
  //     "MID_LONG_TERM_MANAGE", // 중/장기 관리
  //     "인터뷰2", //  인터뷰
  //     "인터뷰3", // interview3
  //     "for_interview", // 인터뷰 4
  //     "CHECK_B", // CHECK_B
  //     "APPROVE_E", // APPROVE_E
  //     "NEGOTIATION",
  //   ],
  //   addAble: false,
  // },
  {
    label: "발굴 사업부",
    value: "departmentNames",
    list: [
      // "MX", "DX", "디자인"
    ],
    addAble: true,
    isOption: true,
    identifier: "DEPARTMENT",
  },
  {
    label: "발굴 담당자",
    value: "founderNames",
    list: [],
    addAble: true,
    isOption: true,
    identifier: "MEMBER",
  },
  {
    label: "기술 키워드",
    value: "techSkillKeywords",
    list: [],
    addAble: true,
    isOption: false,
    identifier: "TECH_SKILL",
  },
];

export default filterItemsData;
