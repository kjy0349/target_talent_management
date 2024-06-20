"use client";
import PersonalInformationForm from "@/components/profile/register/PersonalInformationForm";
import CautionsModal from "@/components/profile/register/CautionsModal";

export default function TalentRegisterLayout() {
  return (
    <>
      <CautionsModal />
      <div className="p-10">
        <PersonalInformationForm />
      </div>
    </>
  );
}
