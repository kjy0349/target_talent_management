import techmapProjectTable from "@/components/techmap/techmapProjectTable";
export default function techmapProjectPage({
  params,
}: {
  params: { slug: number };
}) {
  return (
    <>
      {/* 처음에는 project가 나타난다. */}
      <div className="p-5">
        <techmapProjectTable techmapId={params.slug} />
      </div>
    </>
  );
}
