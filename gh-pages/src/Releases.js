/* eslint-disable react/jsx-no-target-blank */
import React, {Component} from "react";
import ReactMarkdown from "react-markdown";

const {Octokit} = require("@octokit/rest")
const octokit = new Octokit();

function bytesToHuman(size) {
  let i = -1;
  const sizeUnits = ['kB', 'MB', 'GB'];
  do {
    size = size / 1024;
    i++;
  } while (size > 1024);

  return Math.max(size, 0.1).toFixed(1) + ' ' + sizeUnits[i];
}

export default class Releases extends Component {
  constructor(props) {
    super(props);
    this.state = {
      releases: []
    };
  }

  componentDidMount() {
    if (this.state.releases.length === 0) {
      octokit.repos.listReleases({
        owner: 'EroticDevelopment',
        repo: 'GuideMe'
      }).then(value => this.setState({
        releases: value.data ?? []
      }));
    }
  }

  render() {
    return (
      <>
        <h1 className="display-4">Application Releases</h1>
        {this.state.releases.length === 0 ? <p className="h5">Loading...</p> : this.state.releases.map(release => {
          let releaseDate = new Date(release.published_at);
          return (
            <div key={release.id}>
              <p className="mb-0 border-bottom">
                <a className="h1" href={release.html_url} target="_blank">
                  {release.name.replace('Release ', '')}
                </a>
                {release.draft &&
                <span className="ml-2 badge badge-danger align-text-top">Draft</span>
                }
                {release.prerelease &&
                <span className="ml-2 badge badge-warning align-text-top">Prerelease</span>
                }
                <span className="h5 float-right mt-3">{releaseDate.toDateString()}</span>
              </p>
              <ReactMarkdown>{release.body}</ReactMarkdown>
              <h2>Assets</h2>
              <ul>
                {release.assets.map(asset => {
                  return (
                    <li key={asset.id}><a href={asset.browser_download_url}>{asset.name}</a> &ndash; <span
                      className="small">{bytesToHuman(asset.size)}</span> &ndash; <span
                      className="small">{asset.download_count} downloads</span></li>
                  );
                })}
              </ul>
            </div>
          );
        })}
      </>
    );
  }
}
