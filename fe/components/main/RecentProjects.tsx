'use client'

import { useState, useEffect } from 'react';
import Spinner from '../common/Spinner';
import Link from 'next/link';
import { HiChevronRight } from 'react-icons/hi';
import RecentProject from './RecentProject';

const RecentAlarms = () => {
  const [loading, setLoading] = useState(true);
  const [recent, setRecent] = useState<number[]>([]);

  useEffect(() => {
    const recentString = localStorage.getItem("recentProjects");
    let recent: number[] = recentString ? JSON.parse(recentString) : [];
    if (recent.length > 5) {
      recent = recent.slice(recent.length - 5);
    }
    setRecent(recent);
    localStorage.setItem("recentProjects", JSON.stringify(recent));

    const timer = setTimeout(() => {
      setLoading(false);
    }, 200);
    return () => clearTimeout(timer);

  }, []);

  return (
    <div className="flex-1 flex flex-col h-72 gap-5 overflow-auto border bg-white scrollbar-hide">
      <div className="sticky top-0 bg-white z-10">
        <p className="text-center text-2xl font-bold pt-5">최근 조회한 프로젝트</p>
      </div>
      {loading ? (
        <Spinner />
      ) : (
        <>
          {
            recent.length === 0 && (
              <p className='text-center mt-10'>조회한 프로젝트가 없습니다.</p>
            )}
          {
            recent.slice().reverse().map((r) => (
              <RecentProject key={r} id={r} />
            ))
          }
        </>
      )}
    </div>
  );
};


export default RecentAlarms;