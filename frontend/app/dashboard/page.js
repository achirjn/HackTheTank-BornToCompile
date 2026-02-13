'use client';

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';

export default function DashboardPage() {
  const router = useRouter();
  const [userEmail, setUserEmail] = useState('');
  const [userName, setUserName] = useState('');
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    // Check if user is authenticated
    const email = localStorage.getItem('userEmail');
    const name = localStorage.getItem('userName');

    if (!email) {
      router.push('/');
    } else {
      setUserEmail(email);
      setUserName(name);
      setIsLoading(false);
    }
  }, [router]);

  const handleLogout = () => {
    localStorage.clear();
    router.push('/');
  };

  if (isLoading) {
    return (
      <div className="flex justify-center items-center min-h-screen text-lg text-indigo-500">
        Loading...
      </div>
    );
  }

  return (
    <div className="min-h-screen flex flex-col">
      <nav className="sticky top-0 bg-white shadow-md px-8 flex justify-between items-center h-[70px]">
        <div className="flex-shrink-0">
          <h2 className="text-2xl font-bold text-indigo-500">HTT</h2>
        </div>
        
        <div className="hidden md:flex items-center">
          <ul className="flex gap-8 list-none">
            <li>
              <a href="#" className="text-gray-600 font-semibold transition-colors border-b-2 border-transparent hover:text-indigo-500 hover:border-indigo-500 text-indigo-500 border-indigo-500">
                Dashboard
              </a>
            </li>
            <li>
              <a href="#" className="text-gray-600 font-semibold transition-colors border-b-2 border-transparent hover:text-indigo-500 hover:border-indigo-500">
                Profile
              </a>
            </li>
            <li>
              <a href="#" className="text-gray-600 font-semibold transition-colors border-b-2 border-transparent hover:text-indigo-500 hover:border-indigo-500">
                Settings
              </a>
            </li>
          </ul>
        </div>

        <div className="flex items-center gap-5">
          <span className="text-gray-600 text-sm hidden sm:inline">{userEmail}</span>
          <button
            onClick={handleLogout}
            className="px-5 py-2 bg-red-500 text-white rounded-md font-semibold transition-colors hover:bg-red-600"
          >
            Logout
          </button>
        </div>
      </nav>

      <div className="flex-1 p-10 md:p-10">
        <div className="mb-10">
          <h1 className="text-4xl font-bold text-gray-800 mb-3">Welcome to HTT Dashboard</h1>
          <p className="text-lg text-gray-600">
            Hello, <span className="text-indigo-500 font-semibold">{userName}</span>! You're successfully logged in.
          </p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          {[
            { icon: '📊', title: 'Analytics', desc: 'View your analytics and performance metrics' },
            { icon: '⚙️', title: 'Settings', desc: 'Manage your account and preferences' },
            { icon: '👥', title: 'Profile', desc: 'Update your profile information' },
            { icon: '📞', title: 'Support', desc: 'Get help from our support team' },
          ].map((card, index) => (
            <div
              key={index}
              className="bg-white p-8 rounded-xl shadow-md transition-all hover:shadow-xl hover:-translate-y-1 cursor-pointer text-center"
            >
              <div className="text-5xl mb-4">{card.icon}</div>
              <h3 className="text-xl font-bold text-gray-800 mb-2">{card.title}</h3>
              <p className="text-gray-600 text-sm leading-relaxed">{card.desc}</p>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}
