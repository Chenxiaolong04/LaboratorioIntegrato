import { type FormEvent, type ChangeEvent, useState } from "react";
import { FaSearch } from "react-icons/fa";
import Input from "./Input";

interface SearchBarProps {
  /** Placeholder text for the input field */
  placeholder?: string;
  /** Callback function triggered when a search is submitted */
  onSearch: (query: string) => void;
  /** Additional CSS classes for styling */
  className?: string;
}

/**
 * SearchBar component.
 *
 * Renders an input field with a submit button for searching. Handles input
 * changes and triggers the onSearch callback with the trimmed query string
 * when submitted.
 *
 * @function SearchBar
 * @param {SearchBarProps} props - Props for the search bar component.
 * @returns {JSX.Element} A search bar with input and submit button.
 */
export default function SearchBar({
  placeholder = "Cerca...",
  onSearch,
  className = "",
}: SearchBarProps) {
  const [query, setQuery] = useState("");

  const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
    setQuery(e.target.value);
  };

  const handleSubmit = (e: FormEvent) => {
    e.preventDefault();
    onSearch(query.trim());
  };

  return (
    <form className={`search-bar ${className}`} onSubmit={handleSubmit}>
      <Input
        name="searchbar"
        type="text"
        value={query}
        onChange={handleChange}
        placeholder={placeholder}
      />
      <button type="submit" className="search-btn">
        <FaSearch size={24}/>
      </button>
    </form>
  );
}
