import { Table } from "flowbite-react";
import { HiDownload } from "react-icons/hi";

interface props {
  title: string;
  author: string;
  views: number;
  createdAt: string;
  fileUrl?: string;
  onClick: () => void;
}

export default function BoardListRow({ title, author, views, createdAt, fileUrl, onClick }: props) {
  return (
    <Table.Row className="cursor-pointer">
      <Table.Cell onClick={onClick} className="cursor-pointer px-6 py-4 hover:underline">{title}</Table.Cell>
      <Table.Cell className="px-6 py-3">{author}</Table.Cell>
      <Table.Cell className="px-6 py-3">{views}</Table.Cell>
      <Table.Cell className="px-6 py-3">{new Date(createdAt).toLocaleDateString()}</Table.Cell>
      <Table.Cell className="px-6 py-3">{fileUrl && <a href={`${process.env.NEXT_PUBLIC_BASE_URL}/${fileUrl}`} target="_blank" download><HiDownload /></a>}</Table.Cell>
    </Table.Row>
  );
}
