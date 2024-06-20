import techmapTable from "@/components/techmap/techmapTable";
export default function techmapPage() {
  return (
    <>
      {/* 처음에는 project가 나타난다. */}
      <div className="p-5">
        <techmapTable />
      </div>
    </>
  );
}
