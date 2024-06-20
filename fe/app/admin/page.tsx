import { StaticsMain } from "./staticsmain";

export default function AdminPage() {
  return (
    <>
      <div className="flex flex-col space-y-5 p-5 ">
        <h3 className="text-xl">통계 요약</h3>
        <div className="flex space-x-5  ">
          <StaticsMain />
        </div>
      </div>
    </>
  );
}
