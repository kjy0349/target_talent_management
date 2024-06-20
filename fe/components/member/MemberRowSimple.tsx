"use client";

import { Badge, Table } from "flowbite-react";
import { MemberAdminFull } from "@/types/admin/Member";

export default function MemberListRow({
  member,
  onClick
}: { member: MemberAdminFull, onClick: () => void }) {
  return (
    <Table.Row className="border-b hover:bg-gray-100 dark:border-gray-600 dark:hover:bg-gray-700" onClick={onClick}>
      <Table.Cell className="whitespace-nowrap px-4 py-2">
        {member.name}
      </Table.Cell>
      <Table.Cell className="whitespace-nowrap px-4 py-2">
        {member.departmentName}
      </Table.Cell>
      {/* <Table.Cell className="whitespace-nowrap px-4 py-2">
        {member.teamName}
      </Table.Cell> */}
      <Table.Cell className="whitespace-nowrap px-4 py-2">
        {member.roleName}
      </Table.Cell>
      <Table.Cell className="whitespace-nowrap px-4 py-2">
        {member.authority}
      </Table.Cell>
    </Table.Row>
  );
}
