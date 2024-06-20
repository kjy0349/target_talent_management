'use client'

import { Badge, Card } from "flowbite-react"
import { useLocalAxios } from "@/app/api/axios"
import { ProjectFull } from "@/types/admin/Project";
import Link from "next/link";
import { useEffect, useState } from "react";
interface props {
  id: number,
}

export default function RecentProject({ id }: props) {
  const localAxios = useLocalAxios();
  const [project, setProject] = useState<ProjectFull>();
  const coloredBadge: string[] = ["blue", "yellow", "green", "red", "purple"];

  const baseSetting = async () => {
    const response = await localAxios.get<ProjectFull>(`/project/${id}`);
    setProject(response.data);
  };

  useEffect(() => { baseSetting() }, [])

  return (
    <Card className="mx-5">
      <Link className="relative w-full flex gap-6" href={`/project/${id}`}>
        <div className="flex flex-row gap-4 items-center flex-1">
          <p className="font-bold text-lg">{project?.title}</p>
          <p className="">{project?.projectDepartment.name}</p>
          <p>{project?.targetYear}</p>
          <div className="absolute right-0 flex items-center space-x-2 p-1">
            {project?.targetJobRanks.map((jr, index) => (
              <Badge
                className="text-md"
                key={index}
                color={coloredBadge[index % 5]}
              >
                {jr}
              </Badge>
            ))}
          </div>
        </div>
      </Link>
    </Card>
  );
}
