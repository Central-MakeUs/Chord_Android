package com.team.chord.core.ui.legal

fun coachCoachTermsDocument(): LegalDocumentContent =
    LegalDocumentContent(
        headline = "코치코치 서비스 이용약관",
        introBlocks =
            listOf(
                LegalDocumentBlock.Heading("구성 개요"),
                LegalDocumentBlock.Paragraph(
                    "1. 목적 및 정의\n" +
                        "2. 회원 가입 및 계정 관리\n" +
                        "3. 서비스 내용 및 제공 범위\n" +
                        "4. 사용자 입력 정보의 책임\n" +
                        "5. 서비스 이용의 제한 및 중단\n" +
                        "6. 지식재산권\n" +
                        "7. 책임의 제한 및 면책\n" +
                        "8. 계약 해지 및 회원 탈퇴\n" +
                        "9. 약관의 변경\n" +
                        "10. 준거법 및 분쟁 해결",
                ),
                LegalDocumentBlock.Heading("항목별 핵심 내용 가이드"),
            ),
        sections =
            listOf(
                LegalDocumentSection(
                    title = "1. 목적 및 정의",
                    blocks =
                        listOf(
                            LegalDocumentBlock.Bullet("본 약관은 코치코치앱(이하 \"서비스\")의 이용 조건 및 절차, 권리·의무를 규정합니다."),
                            LegalDocumentBlock.Bullet("서비스는 카페 운영에 필요한 원가·수익성 분석을 돕기 위한 참고용 정보 제공 목적입니다."),
                            LegalDocumentBlock.Bullet("계산 결과는 법적·세무적·회계적 자문이 아닙니다."),
                        ),
                ),
                LegalDocumentSection(
                    title = "2. 회원 가입 및 계정 관리",
                    blocks =
                        listOf(
                            LegalDocumentBlock.Bullet("회원은 정확한 정보를 입력해야 합니다."),
                            LegalDocumentBlock.Bullet("계정 정보 관리 책임은 사용자에게 있습니다."),
                            LegalDocumentBlock.Bullet("타인의 계정 사용, 양도, 대여를 금지합니다."),
                        ),
                ),
                LegalDocumentSection(
                    title = "3. 서비스 내용 및 제공 범위",
                    blocks =
                        listOf(
                            LegalDocumentBlock.Bullet("사용자가 입력한 메뉴, 재료비, 비용 정보를 바탕으로 원가, 마진율, 공헌이익, 권장 가격 등의 분석 정보를 제공합니다."),
                            LegalDocumentBlock.Bullet("서비스 내용은 운영 정책에 따라 변경·추가·중단될 수 있습니다."),
                        ),
                ),
                LegalDocumentSection(
                    title = "4. 사용자 입력 정보의 책임",
                    blocks =
                        listOf(
                            LegalDocumentBlock.Bullet("모든 분석 결과는 사용자가 입력한 정보에 의존합니다."),
                            LegalDocumentBlock.Bullet("입력 정보의 정확성, 최신성에 대한 책임은 사용자에게 있습니다."),
                            LegalDocumentBlock.Bullet("잘못된 입력으로 인한 손실에 대해 서비스는 책임을 지지 않습니다."),
                        ),
                ),
                LegalDocumentSection(
                    title = "5. 서비스 이용의 제한 및 중단",
                    blocks =
                        listOf(
                            LegalDocumentBlock.Bullet("시스템 점검, 장애, 불가항력 사유 시 서비스가 일시 중단될 수 있습니다."),
                            LegalDocumentBlock.Bullet("회사는 사전 공지 후 또는 불가피한 경우 사후 공지로 서비스 중단이 가능합니다."),
                        ),
                ),
                LegalDocumentSection(
                    title = "6. 지식재산권",
                    blocks =
                        listOf(
                            LegalDocumentBlock.Bullet("서비스에 포함된 UI, 콘텐츠, 분석 로직, 계산 방식의 저작권은 회사에 귀속됩니다."),
                            LegalDocumentBlock.Bullet("사용자는 개인적인 서비스 이용 범위 내에서만 사용할 수 있습니다."),
                        ),
                ),
                LegalDocumentSection(
                    title = "7. 책임의 제한 및 면책",
                    blocks =
                        listOf(
                            LegalDocumentBlock.Bullet("서비스에서 제공되는 분석, 가이드, 권장 가격은 의사결정을 돕는 참고 자료입니다."),
                            LegalDocumentBlock.Bullet("실제 경영 판단, 손익 결과에 대한 책임은 사용자에게 있습니다."),
                            LegalDocumentBlock.Bullet("회사는 간접 손해, 영업 손실, 기대 수익 손실에 대해 책임을 지지 않습니다."),
                            LegalDocumentBlock.Bullet("서비스의 일부 기능은 자동화된 분석 또는 AI 기반 로직을 활용합니다."),
                            LegalDocumentBlock.Bullet("AI 분석 결과는 참고용 정보이며, 항상 정확하거나 최신임을 보장하지 않습니다."),
                            LegalDocumentBlock.Bullet("AI 결과에 대한 최종 판단 및 책임은 사용자에게 있습니다."),
                        ),
                ),
                LegalDocumentSection(
                    title = "8. 계약 해지 및 회원 탈퇴",
                    blocks =
                        listOf(
                            LegalDocumentBlock.Bullet("사용자는 언제든지 앱 내 기능을 통해 회원 탈퇴가 가능합니다."),
                            LegalDocumentBlock.Bullet("탈퇴 시 관련 법령에 따라 일부 정보는 보관될 수 있습니다."),
                            LegalDocumentBlock.Bullet("구체적 보관 내용은 개인정보 처리방침을 따릅니다."),
                        ),
                ),
                LegalDocumentSection(
                    title = "9. 약관의 변경",
                    blocks =
                        listOf(
                            LegalDocumentBlock.Bullet("약관 변경 시 앱 내 공지 또는 기타 합리적인 방법으로 고지합니다."),
                            LegalDocumentBlock.Bullet("변경 후에도 서비스를 계속 이용할 경우 동의로 간주합니다."),
                        ),
                ),
                LegalDocumentSection(
                    title = "10. 준거법 및 분쟁 해결",
                    blocks = listOf(LegalDocumentBlock.Bullet("본 약관은 대한민국 법을 준거법으로 합니다.")),
                ),
            ),
    )

fun coachCoachPrivacyDocument(): LegalDocumentContent =
    LegalDocumentContent(
        headline = "개인정보 수집 및 이용 동의",
        introBlocks =
            listOf(
                LegalDocumentBlock.Paragraph(
                    "본 개인정보 처리방침은 코치코치앱(이하 \"회사\")이 제공하는 서비스와 관련하여, " +
                        "이용자의 개인정보를 어떻게 수집·이용·보관·처리하는지를 설명합니다.",
                ),
            ),
        sections =
            listOf(
                LegalDocumentSection(
                    title = "1. 수집하는 개인정보 항목",
                    blocks =
                        listOf(
                            LegalDocumentBlock.Paragraph("회사는 회원가입 및 서비스 제공을 위해 다음의 정보를 수집합니다."),
                            LegalDocumentBlock.Bullet("아이디", prefix = "-"),
                            LegalDocumentBlock.Bullet("비밀번호(암호화하여 저장)", prefix = "-"),
                            LegalDocumentBlock.Bullet("서비스 이용 기록", prefix = "-"),
                        ),
                ),
                LegalDocumentSection(
                    title = "2. 개인정보 수집 및 이용 목적",
                    blocks =
                        listOf(
                            LegalDocumentBlock.Paragraph("회사는 수집한 개인정보를 다음 목적에 한하여 이용합니다."),
                            LegalDocumentBlock.Bullet("회원 식별 및 서비스 이용 관리", prefix = "-"),
                            LegalDocumentBlock.Bullet("원가·수익성 분석 등 서비스 제공", prefix = "-"),
                            LegalDocumentBlock.Bullet("서비스 오류 확인 및 개선", prefix = "-"),
                        ),
                ),
                LegalDocumentSection(
                    title = "3. 개인정보 보관 및 이용 기간",
                    blocks =
                        listOf(
                            LegalDocumentBlock.Paragraph(
                                "회사는 개인정보를 원칙적으로 회원 탈퇴 시까지 보관합니다. 다만, 관련 법령에 따라 보관이 필요할 경우에는 해당 법령에서 정한 기간 동안 보관할 수 있습니다.",
                            ),
                        ),
                ),
                LegalDocumentSection(
                    title = "4. 개인정보의 제3자 제공",
                    blocks =
                        listOf(
                            LegalDocumentBlock.Paragraph("회사는 이용자의 개인정보를 제3자에게 제공하지 않습니다."),
                        ),
                ),
                LegalDocumentSection(
                    title = "5. 개인정보 처리 위탁",
                    blocks =
                        listOf(
                            LegalDocumentBlock.Paragraph("회사는 개인정보 처리를 외부 업체에 위탁하지 않습니다."),
                        ),
                ),
                LegalDocumentSection(
                    title = "6. 이용자의 권리",
                    blocks =
                        listOf(
                            LegalDocumentBlock.Paragraph(
                                "이용자는 언제든지 서비스 내 기능을 통해 회원 탈퇴를 요청할 수 있으며, 탈퇴 시 개인정보는 관련 법령에 따라 처리됩니다.",
                            ),
                        ),
                ),
                LegalDocumentSection(
                    title = "7. 개인정보 보호를 위한 조치",
                    blocks =
                        listOf(
                            LegalDocumentBlock.Paragraph("회사는 개인정보 보호를 위해 비밀번호 암호화 등 합리적인 보안 조치를 적용하고 있습니다."),
                        ),
                ),
                LegalDocumentSection(
                    title = "8. 개인정보 관련 문의",
                    blocks =
                        listOf(
                            LegalDocumentBlock.Paragraph("개인정보 처리와 관련한 문의는 앱 내 고객문의 채널(coach.operation@gmail.com)을 통해 접수할 수 있습니다."),
                        ),
                ),
                LegalDocumentSection(
                    title = "9. 개인정보 처리방침의 변경",
                    blocks =
                        listOf(
                            LegalDocumentBlock.Paragraph("본 개인정보 처리방침은 관련 법령 또는 서비스 정책 변경에 따라 수정될 수 있으며, 변경 시 앱 내 공지를 통해 안내합니다."),
                        ),
                ),
            ),
    )
